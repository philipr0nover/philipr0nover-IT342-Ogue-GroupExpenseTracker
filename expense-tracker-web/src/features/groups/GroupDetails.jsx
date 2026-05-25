import { useParams, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import axios from "axios";
import { addMember, getMembers } from "../../services/groupMemberService";
import { getExpensesByGroup, addExpense } from "../../services/expenseService";

const BASE_URL = "https://groupexpensetracker-backend.onrender.com/api/v1";

function GroupDetails() {

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

  const [groupCreatorId, setGroupCreatorId] = useState(null);
  const [currentUserId, setCurrentUserId] = useState(null);

  const totalAmount = expenses.reduce((sum, e) => sum + (e.amount || 0), 0);

  const isCreator = currentUserId !== null &&
                    groupCreatorId !== null &&
                    Number(currentUserId) === Number(groupCreatorId);

  useEffect(() => {
    try {
      const user = JSON.parse(localStorage.getItem("user"));
      if (user?.id) setCurrentUserId(user.id);
    } catch {
      setCurrentUserId(null);
    }
  }, []);

  useEffect(() => {
    let isMounted = true;

    const loadData = async () => {
      try {
        const res = await axios.get(`${BASE_URL}/groups/${id}`);
        if (isMounted) {
          console.log("[GroupDetails] group data:", res.data);
          console.log("[GroupDetails] createdBy:", res.data?.createdBy);
          if (res.data?.createdBy != null) {
            setGroupCreatorId(res.data.createdBy);
          }
        }
      } catch (err) {
        console.warn("[GroupDetails] failed to fetch group:", err?.response?.status, err?.message);
      }

      try {
        setLoadingMembers(true);
        const res = await getMembers(id);
        if (isMounted) setMembers(Array.isArray(res.data) ? res.data : []);
      } catch {
        if (isMounted) setMembers([]);
      } finally {
        if (isMounted) setLoadingMembers(false);
      }

      try {
        setLoadingExpenses(true);
        const res = await getExpensesByGroup(id);
        if (isMounted) setExpenses(Array.isArray(res.data) ? res.data : []);
      } catch {
        if (isMounted) setExpenses([]);
      } finally {
        if (isMounted) setLoadingExpenses(false);
      }
    };

    loadData();
    return () => { isMounted = false; };
  }, [id]);

  // ================= MEMBERS =================

  const searchUser = async () => {
    if (!email.trim()) return;
    try {
      const res = await axios.get(
        `${BASE_URL}/auth/users/search?email=${email.trim()}`
      );
      setFoundUser(res.data || null);
    } catch {
      setFoundUser(null);
    }
  };

  const handleAddMember = async () => {
    if (!foundUser?.id) return;
    try {
      await addMember({ groupId: Number(id), userId: foundUser.id });
      setEmail("");
      setFoundUser(null);
      setShowModal(false);
      const res = await getMembers(id);
      setMembers(Array.isArray(res.data) ? res.data : []);
    } catch {
      alert("Failed to add member");
    }
  };

  // ✅ FIXED: was calling expenses URL instead of group-members
  const handleRemoveMember = async (memberId) => {
    if (!currentUserId) { alert("Session expired. Please log in again."); return; }
    try {
      await axios.delete(
        `${BASE_URL}/group-members/${memberId}?requesterId=${currentUserId}`
      );
      setMembers(prev => prev.filter(m => m.id !== memberId));
    } catch (err) {
      const msg = err.response?.data?.message || "Failed to remove member.";
      alert(msg);
    }
  };

  // ================= EXPENSES =================

  const handleAddExpense = async () => {
    if (!title.trim() || !amount) return;

    let user = null;
    try { user = JSON.parse(localStorage.getItem("user")); } catch { user = null; }
    if (!user?.id) { alert("Session expired. Please log in again."); return; }

    try {
      await addExpense({
        description: title.trim(),
        amount: Number(amount),
        groupId: Number(id),
        paidBy: user.id
      });
      setTitle("");
      setAmount("");
      setShowExpenseModal(false);
      const res = await getExpensesByGroup(id);
      setExpenses(Array.isArray(res.data) ? res.data : []);
    } catch {
      alert("Failed to add expense");
    }
  };

  const handleDeleteExpense = async (expenseId) => {
    if (!currentUserId) { alert("Session expired. Please log in again."); return; }
    try {
      await axios.delete(
        `${BASE_URL}/expenses/${expenseId}?requesterId=${currentUserId}`
      );
      setExpenses(prev => prev.filter(e => e.id !== expenseId));
    } catch (err) {
      const msg = err.response?.data?.message || "Failed to delete expense.";
      alert(msg);
    }
  };

  // ================= UI =================

  return (
    <div style={styles.wrapper}>

      <button style={styles.backBtn} onClick={() => navigate("/groups")}>
        ← Back to groups
      </button>

      <div style={styles.header}>
        <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
          <h2 style={styles.title}>Group #{id}</h2>
          <span style={styles.memberPill}>
            {members.length} member{members.length !== 1 ? "s" : ""}
          </span>
        </div>

        <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
          <div style={styles.totalBox}>
            Total: ₱{totalAmount.toLocaleString()}
          </div>
          {isCreator && (
            <button style={styles.mainBtn} onClick={() => setShowModal(true)}>
              + Add Member
            </button>
          )}
        </div>
      </div>

      <div style={styles.grid}>

        <div style={styles.card}>
          <div style={styles.cardHead}>
            <h3 style={styles.cardTitle}>Members</h3>
          </div>

          {loadingMembers ? (
            <p style={styles.empty}>Loading...</p>
          ) : members.length === 0 ? (
            <p style={styles.empty}>No members yet</p>
          ) : (
            members.map(m => (
              <div key={m.id} style={styles.memberRow}>
                <div style={styles.avatar}>
                  {m.user?.firstname?.[0]?.toUpperCase() || "U"}
                </div>
                <div style={{ flex: 1 }}>
                  <div style={styles.memberName}>
                    {m.user ? `${m.user.firstname} ${m.user.lastname}` : "Unknown"}
                  </div>
                  {m.user?.email && (
                    <div style={styles.memberEmail}>{m.user.email}</div>
                  )}
                </div>
                {isCreator && m.user?.id !== currentUserId && (
                  <button
                    style={styles.removeBtn}
                    onClick={() => handleRemoveMember(m.id)}
                    title="Remove member"
                  >
                    ✕
                  </button>
                )}
              </div>
            ))
          )}
        </div>

        <div style={styles.card}>
          <div style={styles.cardHead}>
            <h3 style={styles.cardTitle}>Shared Expenses</h3>
            <button style={styles.addBtn} onClick={() => setShowExpenseModal(true)}>
              + Add
            </button>
          </div>

          {loadingExpenses ? (
            <p style={styles.empty}>Loading...</p>
          ) : expenses.length === 0 ? (
            <p style={styles.empty}>No expenses yet</p>
          ) : (
            <>
              {expenses.map(exp => (
                <div key={exp.id} style={styles.expenseRow}>
                  <div>
                    <div style={styles.expenseTitle}>{exp.description}</div>
                    <div style={styles.expenseDate}>
                      🗓 {exp.createdAt?.split("T")[0] || "—"}
                    </div>
                  </div>
                  <div style={{ display: "flex", alignItems: "center", gap: "10px" }}>
                    <span style={styles.amount}>₱{exp.amount || 0}</span>
                    {isCreator && (
                      <button
                        style={styles.removeBtn}
                        onClick={() => handleDeleteExpense(exp.id)}
                        title="Delete expense"
                      >
                        🗑
                      </button>
                    )}
                  </div>
                </div>
              ))}

              <div style={styles.totalRow}>
                <span style={styles.totalLabel}>Total</span>
                <span style={styles.totalAmount}>
                  ₱{totalAmount.toLocaleString()}
                </span>
              </div>
            </>
          )}
        </div>

      </div>

      {showModal && (
        <div style={styles.overlay}>
          <div style={styles.modal}>
            <div style={styles.modalHeader}>
              <h3 style={styles.modalTitle}>Add Member</h3>
              <button
                style={styles.closeBtn}
                onClick={() => { setShowModal(false); setEmail(""); setFoundUser(null); }}
              >
                ✕
              </button>
            </div>

            <input
              style={styles.input}
              placeholder="Enter email address"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              onKeyDown={(e) => e.key === "Enter" && searchUser()}
            />

            <button style={styles.primaryBtn} onClick={searchUser}>
              Search
            </button>

            {foundUser && (
              <div style={styles.foundCard}>
                <div style={styles.avatar}>
                  {foundUser.firstname?.[0]?.toUpperCase() || "U"}
                </div>
                <div style={{ flex: 1 }}>
                  <div style={styles.memberName}>
                    {foundUser.firstname} {foundUser.lastname}
                  </div>
                  <div style={styles.memberEmail}>{foundUser.email}</div>
                </div>
                <button style={styles.confirmBtn} onClick={handleAddMember}>
                  Add
                </button>
              </div>
            )}

            <button
              style={styles.cancelBtn}
              onClick={() => { setShowModal(false); setEmail(""); setFoundUser(null); }}
            >
              Cancel
            </button>
          </div>
        </div>
      )}

      {showExpenseModal && (
        <div style={styles.overlay}>
          <div style={styles.modal}>
            <div style={styles.modalHeader}>
              <h3 style={styles.modalTitle}>Add Expense</h3>
              <button
                style={styles.closeBtn}
                onClick={() => { setShowExpenseModal(false); setTitle(""); setAmount(""); }}
              >
                ✕
              </button>
            </div>

            <input
              style={styles.input}
              placeholder="Expense title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
            />

            <div style={styles.amountWrapper}>
              <span style={styles.pesoSign}>₱</span>
              <input
                style={styles.amountInput}
                type="number"
                placeholder="0.00"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
              />
            </div>

            <button style={styles.primaryBtn} onClick={handleAddExpense}>
              Save Expense
            </button>

            <button
              style={styles.cancelBtn}
              onClick={() => { setShowExpenseModal(false); setTitle(""); setAmount(""); }}
            >
              Cancel
            </button>
          </div>
        </div>
      )}

    </div>
  );
}

export default GroupDetails;

const styles = {
  wrapper: { padding: "24px", background: "#f5f6fa", minHeight: "100vh" },
  backBtn: {
    display: "inline-flex", alignItems: "center", gap: "6px",
    padding: "7px 14px", background: "white", border: "1px solid #e0e0e0",
    borderRadius: "8px", fontSize: "13px", color: "#555", cursor: "pointer",
    marginBottom: "20px", fontWeight: "400"
  },
  header: { display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "20px" },
  title: { fontSize: "22px", fontWeight: "600", color: "#16a085" },
  memberPill: {
    fontSize: "12px", color: "#085041", background: "#E1F5EE",
    padding: "3px 10px", borderRadius: "20px", fontWeight: "500", border: "1px solid #9FE1CB"
  },
  totalBox: {
    background: "#E1F5EE", color: "#085041", padding: "7px 14px",
    borderRadius: "8px", fontSize: "14px", fontWeight: "500", border: "1px solid #9FE1CB"
  },
  mainBtn: {
    display: "inline-flex", alignItems: "center", gap: "6px",
    background: "#16a085", color: "#fff", padding: "9px 16px",
    borderRadius: "8px", border: "none", cursor: "pointer", fontSize: "13px", fontWeight: "500"
  },
  grid: { display: "grid", gridTemplateColumns: "1fr 1fr", gap: "16px" },
  card: {
    background: "#fff", padding: "20px", borderRadius: "12px",
    border: "1px solid #efefef", boxShadow: "0 1px 4px rgba(0,0,0,0.04)"
  },
  cardHead: {
    display: "flex", justifyContent: "space-between", alignItems: "center",
    paddingBottom: "12px", marginBottom: "4px", borderBottom: "1px solid #f0f0f0"
  },
  cardTitle: { fontSize: "15px", fontWeight: "600", color: "#333" },
  addBtn: {
    background: "#16a085", color: "#fff", border: "none",
    padding: "6px 12px", borderRadius: "6px", cursor: "pointer", fontSize: "12px", fontWeight: "500"
  },
  memberRow: { display: "flex", alignItems: "center", gap: "10px", padding: "11px 0", borderBottom: "1px solid #f5f5f5" },
  avatar: {
    width: "34px", height: "34px", borderRadius: "50%", background: "#E1F5EE", color: "#085041",
    display: "flex", alignItems: "center", justifyContent: "center",
    fontSize: "13px", fontWeight: "600", flexShrink: 0
  },
  memberName: { fontSize: "14px", fontWeight: "500", color: "#333" },
  memberEmail: { fontSize: "12px", color: "#aaa", marginTop: "1px" },
  removeBtn: {
    background: "#fff0f0", color: "#c0392b", border: "none",
    width: "28px", height: "28px", borderRadius: "6px", cursor: "pointer",
    fontSize: "12px", display: "flex", alignItems: "center", justifyContent: "center", flexShrink: 0
  },
  expenseRow: { display: "flex", justifyContent: "space-between", alignItems: "center", padding: "11px 0", borderBottom: "1px solid #f5f5f5" },
  expenseTitle: { fontSize: "14px", fontWeight: "500", color: "#333" },
  expenseDate: { fontSize: "11px", color: "#bbb", marginTop: "3px" },
  amount: { fontSize: "14px", fontWeight: "600", color: "#16a085" },
  totalRow: { display: "flex", justifyContent: "space-between", alignItems: "center", paddingTop: "12px", marginTop: "8px", borderTop: "1px solid #e8e8e8" },
  totalLabel: { fontSize: "13px", fontWeight: "600", color: "#555" },
  totalAmount: { fontSize: "16px", fontWeight: "700", color: "#16a085" },
  empty: { color: "#ccc", fontSize: "13px", padding: "10px 0" },
  overlay: { position: "fixed", top: 0, left: 0, width: "100%", height: "100%", background: "rgba(0,0,0,0.35)", display: "flex", justifyContent: "center", alignItems: "center", zIndex: 1000 },
  modal: { background: "#fff", padding: "24px", borderRadius: "14px", width: "380px", display: "flex", flexDirection: "column", gap: "12px", boxShadow: "0 8px 30px rgba(0,0,0,0.12)" },
  modalHeader: { display: "flex", justifyContent: "space-between", alignItems: "center" },
  modalTitle: { fontSize: "16px", fontWeight: "600", color: "#333" },
  closeBtn: { background: "none", border: "none", fontSize: "16px", color: "#aaa", cursor: "pointer", padding: "2px 6px", borderRadius: "4px" },
  input: { padding: "10px 12px", borderRadius: "8px", border: "1px solid #e0e0e0", fontSize: "14px", outline: "none", width: "100%" },
  amountWrapper: { display: "flex", alignItems: "center", border: "1px solid #e0e0e0", borderRadius: "8px", overflow: "hidden" },
  pesoSign: { padding: "10px 12px", background: "#f5f5f5", color: "#555", fontSize: "14px", borderRight: "1px solid #e0e0e0" },
  amountInput: { flex: 1, padding: "10px 12px", border: "none", fontSize: "14px", outline: "none" },
  foundCard: { display: "flex", alignItems: "center", gap: "10px", padding: "12px", background: "#f9f9f9", borderRadius: "8px", border: "1px solid #eee" },
  confirmBtn: { background: "#16a085", color: "#fff", border: "none", padding: "6px 14px", borderRadius: "6px", cursor: "pointer", fontSize: "13px", fontWeight: "500" },
  primaryBtn: { background: "#16a085", color: "#fff", padding: "10px", borderRadius: "8px", border: "none", cursor: "pointer", fontSize: "14px", fontWeight: "500" },
  cancelBtn: { background: "#f5f5f5", color: "#666", padding: "10px", borderRadius: "8px", border: "none", cursor: "pointer", fontSize: "14px" }
};