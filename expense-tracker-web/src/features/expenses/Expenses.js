import { useState } from "react";
import Sidebar from "../../components/Sidebar";
import HeaderBar from "../../components/HeaderBar";
import AddExpenseForm from "./AddExpenseForm";
import ExpenseTable from "./ExpenseTable";

function Expenses() {

  const [refresh, setRefresh] = useState(false);

  const handleRefresh = () => {
    setRefresh(prev => !prev);
  };

  return (
    <div style={{ display: "flex" }}>

      {/* Sidebar */}
      <Sidebar />

      {/* Main Content */}
      <div
        style={{
          flex: 1,
          padding: "30px",
          backgroundColor: "#f5f6fa",
          minHeight: "100vh"
        }}
      >

        <HeaderBar title="Expenses" />

        {/* Form Card */}
        <div
          style={{
            backgroundColor: "white",
            padding: "20px",
            borderRadius: "10px",
            marginBottom: "20px",
            boxShadow: "0 2px 8px rgba(0,0,0,0.1)"
          }}
        >
          <AddExpenseForm onSuccess={handleRefresh} />
        </div>

        {/* Table Card */}
        <div
          style={{
            backgroundColor: "white",
            padding: "20px",
            borderRadius: "10px",
            boxShadow: "0 2px 8px rgba(0,0,0,0.1)"
          }}
        >
          <ExpenseTable refresh={refresh} />
        </div>

      </div>

    </div>
  );
}

export default Expenses;