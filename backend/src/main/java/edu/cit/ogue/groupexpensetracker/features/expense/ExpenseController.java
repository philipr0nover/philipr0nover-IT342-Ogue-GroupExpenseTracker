package edu.cit.ogue.groupexpensetracker.features.expense;

import org.springframework.http.ResponseEntity;
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

    @GetMapping("/user/{userId}")
    public List<Expense> getByUser(@PathVariable Long userId) {
        return expenseService.getByUser(userId);
    }

    @PostMapping
    public Expense addExpense(@RequestBody Expense expense) {
        return expenseService.addExpense(expense);
    }

    // ✅ NEW: DELETE /api/v1/expenses/{expenseId}?requesterId={requesterId}
    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long expenseId,
            @RequestParam Long requesterId) {

        expenseService.deleteExpense(expenseId, requesterId);
        return ResponseEntity.noContent().build();
    }
}