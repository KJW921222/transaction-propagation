package com.example.tx;

import com.example.tx.model.Item;
import com.example.tx.model.ItemDetail;
import com.example.tx.repository.TestRepository;
import com.example.tx.service.TestChildService;
import com.example.tx.service.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TxApplicationTests {

	@Autowired
	private TestRepository testRepository;
	@Autowired
	private TestService testService;
	@Autowired
	private TestChildService testSubService;

	private ItemDetail itemDetail;
	private Item item;
	private int countAllItems;
	private int countAllItemDetails;

	@BeforeEach
	void setUp() {
		itemDetail = ItemDetail.builder()
				.size("M").stock(100l).build();

		item = Item.builder()
				.price(new BigDecimal(20000))
				.itemDetails(Arrays.asList(itemDetail))
				.build();

		countAllItems = testRepository.countAllItems();
		countAllItemDetails = testRepository.countAllItemDetails();
	}

	/**
	 * 부모 트랜잭션에 합류, 기대결과 : 부모, 자식 롤백
	 */
	@Test
	@DisplayName("REQUIRED")
	public void addItemRequired() {
		item.setName("tx propagation required");

		assertThatThrownBy(() -> testService.addItemRequired(item))
				.isInstanceOf(UnexpectedRollbackException.class);

		assertThat(testRepository.countAllItems()).isEqualTo(countAllItems);
		assertThat(testRepository.countAllItemDetails()).isEqualTo(countAllItemDetails);
	}

	/**
	 * 새로운 트랜잭션 수행, 기대결과 : 자식만 롤백
	 */
	@Test
	@DisplayName("REQUIRES_NEW")
	public void addItemRequiresNew(){
		item.setName("tx propagation requires_new");

		testService.addItemRequiresNew(item);

		assertThat(testRepository.countAllItems()).isNotEqualTo(countAllItems);
		assertThat(testRepository.countAllItemDetails()).isEqualTo(countAllItemDetails);
	}

	/**
	 * 종속된 트랜잭션에서 자식 롤백, 기대결과 : 자식만 롤백
	 */
	@Test
	@DisplayName("NESTED")
	public void addItemNested(){
		item.setName("tx propagation nested");

		testService.addItemNested(item);

		assertThat(testRepository.countAllItems()).isNotEqualTo(countAllItems);
		assertThat(testRepository.countAllItemDetails()).isEqualTo(countAllItemDetails);
	}

	/**
	 * 종속된 트랜잭션에서 부모 롤백, 기대결과 : 부모, 자식 롤백
	 */
	@Test
	@DisplayName("NESTED PARENT ROLLBACK")
	public void addItemNestedParentRollback(){
		item.setName("tx propagation nested parent");

		testService.addItemNestedParentRollback(item);

		assertThat(testRepository.countAllItems()).isEqualTo(countAllItems);
		assertThat(testRepository.countAllItemDetails()).isEqualTo(countAllItemDetails);
	}

	/**
	 * 부모 트랜잭션이 있으면, REQUIRED처럼 동작
	 */
	@Test
	@DisplayName("MANDATORY")
	public void addItemMandatory(){
		item.setName("tx propagation mandatory");

		assertThatThrownBy(() -> testService.addItemMandatory(item))
				.isInstanceOf(UnexpectedRollbackException.class);

		assertThat(testRepository.countAllItems()).isEqualTo(countAllItems);
		assertThat(testRepository.countAllItemDetails()).isEqualTo(countAllItemDetails);
	}

	/**
	 * 의무적으로 부모 트랜잭션이 있어야한다, 기대결과 : IllegalTransactionStateException 발생
	 */
	@Test
	@DisplayName("MANDATORY NOT EXIST PARENT")
	public void addItemMandatoryNotExistParent(){
		item.setName("tx propagation mandatory");

		assertThatThrownBy(() -> testService.addItemMandatoryNotExistParent(item))
				.isInstanceOf(IllegalTransactionStateException.class);

		assertThat(testRepository.countAllItems()).isEqualTo(countAllItems);
		assertThat(testRepository.countAllItemDetails()).isEqualTo(countAllItemDetails);
	}

	/**
	 * 부모 트랜잭션이 있으면 합류, 기대결과 : 부모, 자식 롤백
	 */
	@Test
	@DisplayName("SUPPORTS")
	public void addItemSupports(){
		item.setName("tx propagation supports");

		assertThatThrownBy(() -> testService.addItemSupports(item))
				.isInstanceOf(UnexpectedRollbackException.class);

		assertThat(testRepository.countAllItems()).isEqualTo(countAllItems);
		assertThat(testRepository.countAllItemDetails()).isEqualTo(countAllItemDetails);
	}

	/**
	 * 부모 트랜잭션이 없으면 논트랙잭션으로 실행, 기대결과 : 부모, 자식 롤백안됨
	 */
	@Test
	@DisplayName("SUPPORTS NOT EXIST PARENT")
	public void addItemSupportsNotExistParent(){
		item.setName("tx propagation supports");

		testService.addItemSupportsNotExistParent(item);

		assertThat(testRepository.countAllItems()).isNotEqualTo(countAllItems);
		assertThat(testRepository.countAllItemDetails()).isNotEqualTo(countAllItemDetails);
	}

	/**
	 * 부모는 일시중지, 자식은 논트랜잭션으로 동작, 기대결과 : 부모 롤백안됨, 자식은 논트랜잭션이므로 롤백안됨
	 */
	@Test
	@DisplayName("NOT SUPPORTED")
	public void addItemNotSupported(){
		item.setName("tx propagation not supported");

		testService.addItemNotSupported(item);

		assertThat(testRepository.countAllItems()).isNotEqualTo(countAllItems);
		assertThat(testRepository.countAllItemDetails()).isNotEqualTo(countAllItemDetails);
	}

	/**
	 * 부모 트랜잭션이 없어야한다, 기대결과 : IllegalTransactionStateException 발생
	 */
	@Test
	@DisplayName("NEVER")
	public void addItemNever(){
		item.setName("tx propagation never");

		assertThatThrownBy(() -> testService.addItemNever(item))
				.isInstanceOf(IllegalTransactionStateException.class);

		assertThat(testRepository.countAllItems()).isEqualTo(countAllItems);
		assertThat(testRepository.countAllItemDetails()).isEqualTo(countAllItemDetails);
	}

}
