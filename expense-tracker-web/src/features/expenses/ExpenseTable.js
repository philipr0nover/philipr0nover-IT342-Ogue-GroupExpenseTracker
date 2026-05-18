import { useEffect, useState } from "react";
import axios from "axios";

export default function ExpenseTable({ refresh }) {

  const [expenses, setExpenses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    let isMounted = true;

    const fetchExpenses = async () => {
      try {
        setLoading(true);
        setError(null);

        const res = await axios.get("http://localhost:8080/api/v1/expenses");

        if (!isMounted) return;

        // ✅ CLEAN DATA
        const cleanData = (res.data || []).filter(
          (e) => e.description && e.description.trim() !== "" && e.amount
        );

        setExpenses(cleanData);

      } catch (err) {
        if (!isMounted) return;

        console.error("Fetch error:", err);
        setError("Failed to load expenses");
      } finally {
        if (isMounted) setLoading(false);
      }
    };

    fetchExpenses();

    return () => {
      isMounted = false;
    };

  }, [refresh]);

  // ✅ LOADING STATE (no flicker)
  if (loading) {
    return <p style={{ padding: "20px" }}>Loading expenses...</p>;
  }

  // ✅ ERROR STATE (no alert spam)
  if (error) {
    return (
      <p style={{ padding: "20px", color: "red" }}>
        {error}
      </p>
    );
  }

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
            <td colSpan="3" style={{ padding: "15px", textAlign: "center" }}>
              No expenses yet
            </td>
          </tr>
        ) : (
          expenses.map((e) => (
            <tr
              key={e.id}
              style={{
                borderBottom: "1px solid #eee",
                transition: "background 0.2s"
              }}
              onMouseEnter={(event) =>
                (event.currentTarget.style.background = "#f9f9f9")
              }
              onMouseLeave={(event) =>
                (event.currentTarget.style.background = "white")
              }
            >
              <td style={{ padding: "12px" }}>
                {e.description || "No title"}
              </td>

              <td
                style={{
                  padding: "12px",
                  fontWeight: "bold",
                  color: "#16a085"
                }}
              >
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