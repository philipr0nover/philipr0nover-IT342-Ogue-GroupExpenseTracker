import { useEffect, useState } from "react";
import axios from "axios";

export default function ExpenseTable({ refresh }) {

  const [expenses, setExpenses] = useState([]);

  const fetchExpenses = async () => {
    try {
      const res = await axios.get("http://localhost:8080/api/v1/expenses/1");
      setExpenses(res.data);
    } catch (err) {
      alert("Error loading expenses");
    }
  };

  useEffect(() => {
    fetchExpenses();
  }, [refresh]);

  return (
    <table
      style={{
        width: "100%",
        borderCollapse: "collapse",
        backgroundColor: "white",
        borderRadius: "10px",
        overflow: "hidden",
        boxShadow: "0 2px 8px rgba(0,0,0,0.1)"
      }}
    >
      <thead style={{ backgroundColor: "#1abc9c", color: "white" }}>
        <tr>
          <th style={{ padding: "12px", textAlign: "left" }}>Title</th>
          <th style={{ padding: "12px", textAlign: "left" }}>Amount</th>
          <th style={{ padding: "12px", textAlign: "left" }}>Date</th>
        </tr>
      </thead>

      <tbody>
        {expenses.length === 0 ? (
          <tr>
            <td colSpan="3" style={{ padding: "15px", textAlign: "center" }}>
              No expenses yet
            </td>
          </tr>
        ) : (
          expenses.map((e) => (
            <tr
              key={e.id}
              style={{ borderBottom: "1px solid #eee", cursor: "pointer" }}
              onMouseEnter={(event) => event.currentTarget.style.background = "#f9f9f9"}
              onMouseLeave={(event) => event.currentTarget.style.background = "white"}
            >
              <td style={{ padding: "12px" }}>{e.description}</td>
              <td style={{ padding: "12px", fontWeight: "bold" }}>₱{e.amount}</td>
              <td style={{ padding: "12px" }}>
                {e.createdAt ? e.createdAt.split("T")[0] : ""}
              </td>
            </tr>
          ))
        )}
      </tbody>
    </table>
  );
}