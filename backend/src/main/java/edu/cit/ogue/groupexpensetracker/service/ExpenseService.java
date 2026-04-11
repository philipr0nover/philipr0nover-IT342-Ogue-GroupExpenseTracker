package edu.cit.ogue.groupexpensetracker.service;

import edu.cit.ogue.groupexpensetracker.entity.Expense;
import edu.cit.ogue.groupexpensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public List<Expense> getByGroup(Long groupId) {
        return expenseRepository.findByGroupId(groupId);
    }
}
