import { useState } from "react";
import axios from "axios";

export default function AddExpenseForm({ onSuccess }) {

  const [title, setTitle] = useState("");
  const [amount, setAmount] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!title || !amount) {
      alert("All fields required");
      return;
    }

    if (parseFloat(amount) <= 0) {
      alert("Amount must be positive");
      return;
    }

    try {
      // 🔥 SAFE PARSE (prevents crash if localStorage is broken)
      const userData = localStorage.getItem("user");
      if (!userData) {
        alert("User not logged in");
        return;
      }

      const user = JSON.parse(userData);

      if (!user?.id) {
        alert("Invalid user session");
        return;
      }

      await axios.post("http://localhost:8080/api/v1/expenses", {
        description: title,
        amount: parseFloat(amount),
        groupId: 1, // keep for now
        paidBy: user.id // ✅ correct
      });

      // 🔥 reset BEFORE refresh (avoids UI glitch)
      setTitle("");
      setAmount("");

      if (onSuccess) onSuccess();

    } catch (err) {
      console.error("Add expense error:", err); // 🔥 see real backend error
      alert("Failed to add expense");
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      style={{
        display: "flex",
        gap: "12px",
        alignItems: "center"
      }}
    >

      <input
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        placeholder="Expense title"
        style={{
          padding: "12px",
          borderRadius: "8px",
          border: "1px solid #ddd",
          flex: 1,
          fontSize: "14px"
        }}
      />

      <input
        type="number"
        value={amount}
        onChange={(e) => setAmount(e.target.value)}
        placeholder="Amount"
        style={{
          padding: "12px",
          borderRadius: "8px",
          border: "1px solid #ddd",
          width: "140px",
          fontSize: "14px"
        }}
      />

      <button
        type="submit"
        style={{
          backgroundColor: "#1abc9c",
          color: "white",
          border: "none",
          padding: "12px 18px",
          borderRadius: "8px",
          cursor: "pointer",
          fontWeight: "600"
        }}
      >
        + Add
      </button>

    </form>
  );
}