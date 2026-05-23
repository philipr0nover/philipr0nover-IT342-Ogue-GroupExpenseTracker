import { useParams, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import axios from "axios";
import { addMember, getMembers } from "../../services/groupMemberService";
import { getExpensesByGroup, addExpense } from "../../services/expenseService";

function GroupDetails(){

  const { id } = useParams();
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [foundUser, setFoundUser] = useState(null);
  const [members, setMembers] = useState([]);
  const [showModal, setShowModal] = useState(false);

  const [expenses, setExpenses] = useState([]);
  const [showExpenseModal, setShowExpenseModal] = useState(false);
  const [title, setTitle] = useState("");
  const [amount, setAmount] = useState("");

  const [loadingMembers, setLoadingMembers] = useState(true);
  const [loadingExpenses, setLoadingExpenses] = useState(true);

  useEffect(() => {
    fetchMembers();
    fetchExpenses();
  }, [id]);

  // ================= MEMBERS =================

  const fetchMembers = async () => {
    try {
      setLoadingMembers(true);
      const res = await getMembers(id);
      setMembers(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      console.error("Members error:", err);
      setMembers([]);
    } finally {
      setLoadingMembers(false);
    }
  };

  const searchUser = async () => {
    if (!email.trim()) return;

    try {
      const res = await axios.get(
        `http://localhost:8080/api/v1/auth/users/search?email=${email.trim()}`
      );
      setFoundUser(res.data || null);
    } catch (err) {
      console.error(err);
      setFoundUser(null);
    }
  };

  const handleAddMember = async () => {
    if (!foundUser || !foundUser.id) return;

    try {
      await addMember({
        groupId: Number(id),
        userId: foundUser.id
      });

      setEmail("");
      setFoundUser(null);
      setShowModal(false);

      fetchMembers();

    } catch (err) {
      console.error("Add member failed:", err);
      alert("Failed to add member");
    }
  };

  // ================= EXPENSES =================

  const fetchExpenses = async () => {
    try {
      setLoadingExpenses(true);
      const res = await getExpensesByGroup(id);
      setExpenses(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      console.error("Expenses error:", err);
      setExpenses([]);
    } finally {
      setLoadingExpenses(false);
    }
  };

  const handleAddExpense = async () => {
    if (!title.trim() || !amount) return;

    try {
      const user = JSON.parse(localStorage.getItem("user"));

      await addExpense({
        description: title.trim(),
        amount: Number(amount),
        groupId: Number(id),
        paidBy: user.id
      });

      setTitle("");
      setAmount("");
      setShowExpenseModal(false);

      fetchExpenses();

    } catch (err) {
      console.error("Add expense failed:", err);
      alert("Failed to add expense");
    }
  };

  // ================= UI =================

  return(
    <div style={styles.wrapper}>

      <button style={styles.backBtn} onClick={() => navigate("/groups")}>
        ← Back
      </button>

      <div style={styles.header}>
        <h2 style={styles.title}>Group #{id}</h2>

        <button
          style={styles.addMainBtn}
          onClick={() => setShowModal(true)}
        >
          + Add Member
        </button>
      </div>

      <div style={styles.grid}>

        {/* MEMBERS */}
        <div style={styles.card}>
          <h3>Members</h3>

          {loadingMembers ? (
            <p style={styles.empty}>Loading...</p>
          ) : members.length === 0 ? (
            <p style={styles.empty}>No members yet</p>
          ) : (
            members.map(m => (
              <div key={m.id} style={styles.memberCard}>
                <div style={styles.avatar}>
                  {m.user?.firstname?.[0] || "U"}
                </div>
                <span>
                  {m.user
                    ? `${m.user.firstname} ${m.user.lastname}`
                    : "Unknown User"}
                </span>
              </div>
            ))
          )}
        </div>

        {/* EXPENSES */}
        <div style={styles.card}>
          <div style={styles.cardHeader}>
            <h3>Shared Expenses</h3>
            <button
              style={styles.addExpenseBtn}
              onClick={() => setShowExpenseModal(true)}
            >
              + Add
            </button>
          </div>

          {loadingExpenses ? (
            <p style={styles.empty}>Loading...</p>
          ) : expenses.length === 0 ? (
            <p style={styles.empty}>No expenses yet</p>
          ) : (
            expenses.map(exp => (
              <div key={exp.id} style={styles.expenseCard}>
                <div>
                  <div style={styles.expenseTitle}>
                    {exp.description || "No title"}
                  </div>
                  <div style={styles.expenseDate}>
                    {exp.createdAt ? exp.createdAt.split("T")[0] : ""}
                  </div>
                </div>

                <span style={styles.amount}>
                  ₱{exp.amount || 0}
                </span>
              </div>
            ))
          )}
        </div>

      </div>

      {/* ADD MEMBER MODAL */}
      {showModal && (
        <div style={styles.overlay}>
          <div style={styles.modal}>
            <h3>Add Member</h3>

            <input
              style={styles.input}
              placeholder="Enter email"
              value={email}
              onChange={(e)=>setEmail(e.target.value)}
            />

            <button style={styles.searchBtn} onClick={searchUser}>
              Search
            </button>

            {foundUser && (
              <div style={{ marginTop: "10px" }}>
                <p>{foundUser.firstname} {foundUser.lastname}</p>

                <button
                  style={styles.searchBtn}
                  onClick={handleAddMember}
                >
                  Add to Group
                </button>
              </div>
            )}
          </div>
        </div>
      )}

      {/* ADD EXPENSE MODAL */}
      {showExpenseModal && (
        <div style={styles.overlay}>
          <div style={styles.modal}>
            <h3>Add Expense</h3>

            <input
              style={styles.input}
              placeholder="Title"
              value={title}
              onChange={(e)=>setTitle(e.target.value)}
            />

            <input
              style={styles.input}
              type="number"
              placeholder="Amount"
              value={amount}
              onChange={(e)=>setAmount(e.target.value)}
            />

            <button style={styles.searchBtn} onClick={handleAddExpense}>
              Save
            </button>
          </div>
        </div>
      )}

    </div>
  );
}

export default GroupDetails;

const styles = {
  wrapper: {
    padding: "30px",
    background: "#f5f6fa",
    minHeight: "100vh"
  },
  header: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center"
  },
  title: { color: "#16a085" },
  backBtn: {
    background: "none",
    border: "none",
    color: "#16a085",
    cursor: "pointer",
    marginBottom: "10px"
  },
  addMainBtn: {
    background: "#16a085",
    color: "#fff",
    border: "none",
    padding: "10px",
    borderRadius: "8px",
    cursor: "pointer"
  },
  grid: {
    display: "grid",
    gridTemplateColumns: "1fr 1fr",
    gap: "20px",
    marginTop: "20px"
  },
  card: {
    background: "#fff",
    padding: "20px",
    borderRadius: "10px",
    boxShadow: "0 2px 8px rgba(0,0,0,0.1)"
  },
  memberCard: {
    display: "flex",
    alignItems: "center",
    gap: "10px",
    padding: "10px 0",
    borderBottom: "1px solid #eee"
  },
  avatar: {
    width: "35px",
    height: "35px",
    borderRadius: "50%",
    background: "#16a085",
    color: "#fff",
    display: "flex",
    alignItems: "center",
    justifyContent: "center"
  },
  expenseCard: {
    display: "flex",
    justifyContent: "space-between",
    padding: "10px 0",
    borderBottom: "1px solid #eee"
  },
  expenseTitle: { fontWeight: "600" },
  expenseDate: { fontSize: "12px", color: "#888" },
  amount: { color: "#16a085", fontWeight: "bold" },
  addExpenseBtn: {
    background: "#27ae60",
    color: "#fff",
    border: "none",
    padding: "5px 10px",
    borderRadius: "6px",
    cursor: "pointer"
  },
  empty: { color: "#999" },
  overlay: {
    position: "fixed",
    top: 0,
    left: 0,
    width: "100%",
    height: "100%",
    background: "rgba(0,0,0,0.4)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center"
  },
  modal: {
    background: "#fff",
    padding: "20px",
    borderRadius: "10px",
    width: "350px"
  },
  input: {
    width: "100%",
    padding: "10px",
    marginBottom: "10px"
  },
  searchBtn: {
    width: "100%",
    padding: "10px",
    background: "#16a085",
    color: "#fff",
    border: "none",
    cursor: "pointer"
  }
};