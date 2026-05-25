import { useEffect, useState } from "react";
import Sidebar from "../components/Sidebar";
import HeaderBar from "../components/HeaderBar";
import StatCard from "../components/StatCard";
import ExpenseTable from "../features/expenses/ExpenseTable";

function Dashboard() {

  const [totalExpenses, setTotalExpenses] = useState(0);
  const [totalGroups, setTotalGroups] = useState(0);
  const [expenses, setExpenses] = useState([]);       // ← shared state
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);           // ← track errors visibly

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      setLoading(true);
      setError(null);

      const user = JSON.parse(localStorage.getItem("user"));
      if (!user?.id) {
        setError("No user session found. Please log in again.");
        return;
      }

      // EXPENSES — check res.ok before parsing
      const expenseRes = await fetch(
        `https://groupexpensetracker-backend.onrender.com/api/v1/expenses/user/${user.id}`
      );
      if (!expenseRes.ok) throw new Error(`Expenses failed: ${expenseRes.status}`);
      const expenseData = await expenseRes.json();

      const cleanExpenses = (expenseData || []).filter(
        (e) => e.description?.trim() && e.amount
      );
      setExpenses(cleanExpenses);

      const total = cleanExpenses.reduce((sum, e) => sum + (e.amount || 0), 0);
      setTotalExpenses(total);

      // GROUPS — check res.ok before parsing
      const groupRes = await fetch(
       `https://groupexpensetracker-backend.onrender.com/api/v1/groups/user/${user.id}`
      );
      if (!groupRes.ok) throw new Error(`Groups failed: ${groupRes.status}`);
      const groupData = await groupRes.json();

      setTotalGroups(groupData?.length || 0);

    } catch (err) {
      console.error("Dashboard error:", err);
      setError("Failed to load dashboard data. Please refresh.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ display: "flex" }}>

      <Sidebar />

      <div style={{ flex: 1, padding: "30px", background: "#f5f6fa", minHeight: "100vh" }}>

        <HeaderBar title="Dashboard" />

        {/* ERROR BANNER */}
        {error && (
          <div style={{
            marginTop: "15px",
            padding: "12px 16px",
            background: "#fdecea",
            border: "1px solid #f5c6cb",
            borderRadius: "8px",
            color: "#c0392b"
          }}>
            {error}
            <button
              onClick={fetchStats}
              style={{
                marginLeft: "12px",
                background: "none",
                border: "none",
                color: "#c0392b",
                cursor: "pointer",
                textDecoration: "underline"
              }}
            >
              Retry
            </button>
          </div>
        )}

        {/* STATS */}
        <div style={{
          display: "grid",
          gridTemplateColumns: "repeat(auto-fit, minmax(220px, 1fr))",
          gap: "20px",
          marginTop: "20px"
        }}>
          <StatCard
            title="Total Expenses"
            value={loading ? "..." : `₱${totalExpenses}`}
          />
          <StatCard
            title="Groups"
            value={loading ? "..." : totalGroups}
          />
        </div>

        {/* TABLE — pass expenses down, no duplicate fetch */}
        <div style={{
          marginTop: "25px",
          background: "white",
          padding: "20px",
          borderRadius: "12px",
          boxShadow: "0 2px 10px rgba(0,0,0,0.05)"
        }}>
          <h3 style={{ marginBottom: "15px" }}>Recent Expenses</h3>

          <ExpenseTable expenses={expenses} loading={loading} />
        </div>

      </div>

    </div>
  );
}

export default Dashboard;

const cardWrapper = {
  background: "white",
  padding: "10px",
  borderRadius: "12px",
  boxShadow: "0 2px 10px rgba(0,0,0,0.05)"
};