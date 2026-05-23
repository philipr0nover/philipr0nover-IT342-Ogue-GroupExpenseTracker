import { useEffect, useState } from "react";
import axios from "axios";

export default function ExpenseTable({ expenses: propExpenses, loading: propLoading, refresh }) {

  const [ownExpenses, setOwnExpenses] = useState([]);
  const [ownLoading, setOwnLoading] = useState(false);
  const [error, setError] = useState(null);

  // Only fetch on its own when no expenses are passed from parent
  const isStandalone = propExpenses === undefined;

  useEffect(() => {
    if (!isStandalone) return; // Dashboard is passing data, skip fetch

    const fetchExpenses = async () => {
      try {
        setOwnLoading(true);
        setError(null);

        const user = JSON.parse(localStorage.getItem("user"));
        if (!user?.id) {
          setError("User not found");
          return;
        }

        const res = await axios.get(
          `http://localhost:8080/api/v1/expenses/user/${user.id}`
        );

        const cleanData = (res.data || []).filter(
          (e) => e.description?.trim() && e.amount
        );

        setOwnExpenses(cleanData);

      } catch (err) {
        console.error("Fetch error:", err);
        setError("Failed to load expenses");
      } finally {
        setOwnLoading(false);
      }
    };

    fetchExpenses();
  }, [refresh, isStandalone]); // re-runs when refresh toggles (from Expenses.js)

  // Use parent data if provided, otherwise use own fetched data
  const expenses = isStandalone ? ownExpenses : (propExpenses || []);
  const loading = isStandalone ? ownLoading : (propLoading || false);

  if (loading) return <p style={{ padding: "20px" }}>Loading expenses...</p>;

  if (error) return <p style={{ padding: "20px", color: "red" }}>{error}</p>;

  return (
    <table style={{
      width: "100%",
      borderCollapse: "collapse",
      backgroundColor: "white",
      borderRadius: "10px",
      overflow: "hidden",
      boxShadow: "0 2px 8px rgba(0,0,0,0.1)"
    }}>
      <thead style={{ backgroundColor: "#16a085", color: "white" }}>
        <tr>
          <th style={{ padding: "12px", textAlign: "left" }}>Title</th>
          <th style={{ padding: "12px", textAlign: "left" }}>Amount</th>
          <th style={{ padding: "12px", textAlign: "left" }}>Date</th>
        </tr>
      </thead>

      <tbody>
        {expenses.length === 0 ? (
          <tr>
            <td colSpan="3" style={{ padding: "15px", textAlign: "center", color: "#999" }}>
              No expenses yet
            </td>
          </tr>
        ) : (
          expenses.map((e) => (
            <tr
              key={e.id}
              style={{ borderBottom: "1px solid #eee", transition: "background 0.2s" }}
              onMouseEnter={(ev) => (ev.currentTarget.style.background = "#f9f9f9")}
              onMouseLeave={(ev) => (ev.currentTarget.style.background = "white")}
            >
              <td style={{ padding: "12px" }}>{e.description || "No title"}</td>
              <td style={{ padding: "12px", fontWeight: "bold", color: "#16a085" }}>
                ₱{e.amount}
              </td>
              <td style={{ padding: "12px" }}>
                {e.createdAt ? e.createdAt.split("T")[0] : "—"}
              </td>
            </tr>
          ))
        )}
      </tbody>
    </table>
  );
}