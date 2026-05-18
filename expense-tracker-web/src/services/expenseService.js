import axios from "axios";

const API = "http://localhost:8080/api/v1/expenses";

// ✅ GET EXPENSES BY GROUP
export const getExpensesByGroup = (groupId) => {
  return axios.get(`${API}/group/${groupId}`);
};

// ✅ ADD EXPENSE
export const addExpense = (expense) => {
  return axios.post(API, expense);
};