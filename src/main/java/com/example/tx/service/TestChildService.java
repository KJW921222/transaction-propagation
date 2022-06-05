package com.example.tx.service;

import com.example.tx.model.Item;
import com.example.tx.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@RequiredArgsConstructor
@Service
public class TestChildService {

    private final TestRepository testRepository;

    @Transactional
    public void addItemDetailRequired(Item item) {
        try {
            testRepository.addItemDetail(item.getItemId(), item.getItemDetails());
            throw new RuntimeException();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addItemDetailRequiresNew(Item item) {
        try {
            testRepository.addItemDetail(item.getItemId(), item.getItemDetails());
            throw new RuntimeException();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void addItemDetailNested(Item item) {
        try {
            testRepository.addItemDetail(item.getItemId(), item.getItemDetails());
            throw new RuntimeException();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void addItemDetailNestedParentRollback(Item item) {
        testRepository.addItemDetail(item.getItemId(), item.getItemDetails());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void addItemDetailMandatory(Item item) {
        try {
            testRepository.addItemDetail(item.getItemId(), item.getItemDetails());
            throw new RuntimeException();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void addItemDetailSupports(Item item) {
        try {
            testRepository.addItemDetail(item.getItemId(), item.getItemDetails());
            throw new RuntimeException();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void addItemDetailNotSupported(Item item) {
        try {
            testRepository.addItemDetail(item.getItemId(), item.getItemDetails());
            throw new RuntimeException();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Transactional(propagation = Propagation.NEVER)
    public void addItemDetailNever(Item item) {
        try {
            testRepository.addItemDetail(item.getItemId(), item.getItemDetails());
            throw new RuntimeException();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }
}
