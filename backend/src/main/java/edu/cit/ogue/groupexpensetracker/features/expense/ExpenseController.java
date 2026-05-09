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

    // ✅ ADD EXPENSE
    @PostMapping
    public Expense addExpense(@RequestBody Expense expense) {
        return expenseService.addExpense(expense);
    }

    // ✅ GET EXPENSES BY GROUP
    @GetMapping("/{groupId}")
    public List<Expense> getExpenses(@PathVariable Long groupId) {
        return expenseService.getByGroup(groupId);
    }
}
