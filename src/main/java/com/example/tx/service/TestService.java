package com.example.tx.service;

import com.example.tx.model.Item;
import com.example.tx.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@RequiredArgsConstructor
@Service
public class TestService {

    private final TestRepository testRepository;
    private final TestChildService testChildService;

    @Transactional
    public void addItemRequired(Item item){
        testRepository.addItem(item);
        testChildService.addItemDetailRequired(item);
    }

    @Transactional
    public void addItemRequiresNew(Item item) {
        testRepository.addItem(item);
        testChildService.addItemDetailRequiresNew(item);
    }

    @Transactional
    public void addItemNested(Item item) {
        testRepository.addItem(item);
        testChildService.addItemDetailNested(item);
    }

    @Transactional
    public void addItemNestedParentRollback(Item item) {
        try {
            testRepository.addItem(item);
            throw new RuntimeException();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        testChildService.addItemDetailNestedParentRollback(item);
    }

    @Transactional
    public void addItemMandatory(Item item) {
        testRepository.addItem(item);
        testChildService.addItemDetailMandatory(item);
    }

    public void addItemMandatoryNotExistParent(Item item) {
        testRepository.addItem(item);
        testChildService.addItemDetailMandatory(item);
    }

    @Transactional
    public void addItemSupports(Item item) {
        testRepository.addItem(item);
        testChildService.addItemDetailSupports(item);
    }

    public void addItemSupportsNotExistParent(Item item) {
        testRepository.addItem(item);
        testChildService.addItemDetailSupports(item);
    }

    @Transactional
    public void addItemNotSupported(Item item) {
        testRepository.addItem(item);
        testChildService.addItemDetailNotSupported(item);
    }

    @Transactional
    public void addItemNever(Item item) {
        testRepository.addItem(item);
        testChildService.addItemDetailNever(item);
    }
}
