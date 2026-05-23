package edu.cit.ogue.groupexpensetracker.features.expense;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/group/{groupId}")
    public List<Expense> getByGroup(@PathVariable Long groupId) {
        return expenseService.getByGroup(groupId);
    }

    // ✅ ADD THIS (user-based)
    @GetMapping("/user/{userId}")
    public List<Expense> getByUser(@PathVariable Long userId) {
        return expenseService.getByUser(userId);
    }

    @PostMapping
    public Expense addExpense(@RequestBody Expense expense) {
        return expenseService.addExpense(expense);
    }
}