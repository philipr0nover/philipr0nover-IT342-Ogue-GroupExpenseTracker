import axios from "axios";

const API = "http://localhost:8080/api/v1/expenses";

// ✅ GET EXPENSES BY GROUP (SAFE)
export const getExpensesByGroup = async (groupId) => {
  if (!groupId) {
    throw new Error("groupId is required");
  }

  const res = await axios.get(`${API}/group/${Number(groupId)}`);
  return res;
};

// ✅ ADD EXPENSE (SAFE)
export const addExpense = async (expense) => {
  if (!expense || !expense.description || !expense.amount || !expense.groupId) {
    throw new Error("Invalid expense data");
  }

  const res = await axios.post(API, {
    description: expense.description,
    amount: Number(expense.amount),
    groupId: Number(expense.groupId)
  });

  return res;
};