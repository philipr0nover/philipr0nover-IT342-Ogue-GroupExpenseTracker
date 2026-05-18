import { useEffect, useState } from "react";
import Sidebar from "../components/Sidebar";
import HeaderBar from "../components/HeaderBar";
import StatCard from "../components/StatCard";
import ExpenseTable from "../features/expenses/ExpenseTable";

function Dashboard(){

  const [totalExpenses, setTotalExpenses] = useState(0);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      setLoading(true);

      const user = JSON.parse(localStorage.getItem("user"));

      if (!user || !user.id) {
        setLoading(false);
        return;
      }

      const res = await fetch(
        `http://localhost:8080/api/v1/expenses/user/${user.id}`
      );

      const data = await res.json();

      const total = (data || []).reduce(
        (sum, e) => sum + (e.amount || 0),
        0
      );

      setTotalExpenses(total);

    } catch (err) {
      console.error("Dashboard error:", err);
    } finally {
      setLoading(false);
    }
  };

  return(
    <div style={{ display: "flex" }}>

      <Sidebar/>

      <div
        style={{
          flex: 1,
          padding: "30px",
          background: "#f5f6fa",
          minHeight: "100vh"
        }}
      >

        <HeaderBar title="Dashboard"/>

        {/* 🔥 STATS SECTION */}
        <div
          style={{
            display: "grid",
            gridTemplateColumns: "repeat(auto-fit, minmax(220px, 1fr))",
            gap: "20px",
            marginTop: "20px"
          }}
        >

          <div style={cardWrapper}>
            <StatCard
              title="Total Expenses"
              value={loading ? "..." : `₱${totalExpenses}`}
            />
          </div>

          {/* 🔥 FUTURE CARDS (safe placeholders) */}
          <div style={cardWrapper}>
            <StatCard title="Groups" value="—" />
          </div>

          <div style={cardWrapper}>
            <StatCard title="Pending" value="—" />
          </div>

        </div>

        {/* 🔥 TABLE SECTION */}
        <div
          style={{
            marginTop: "25px",
            background: "white",
            padding: "20px",
            borderRadius: "12px",
            boxShadow: "0 2px 10px rgba(0,0,0,0.05)"
          }}
        >
          <h3 style={{ marginBottom: "15px" }}>
            Recent Expenses
          </h3>

          <ExpenseTable/>
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