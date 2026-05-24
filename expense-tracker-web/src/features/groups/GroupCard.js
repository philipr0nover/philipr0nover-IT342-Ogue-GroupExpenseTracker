import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function GroupCard({ id, name, onDeleted }) {

  const navigate = useNavigate();
  const [showConfirm, setShowConfirm] = useState(false);
  const [deleting, setDeleting] = useState(false);

  const handleDelete = async () => {
    setDeleting(true);
    try {
      const user = JSON.parse(localStorage.getItem("user"));
      await axios.delete(
        `http://localhost:8080/api/v1/groups/${id}?requesterId=${user.id}`
      );
      setShowConfirm(false);
      if (onDeleted) onDeleted(id);
    } catch (err) {
      setDeleting(false);
      setShowConfirm(false);
      const msg = err.response?.data?.message || "Failed to delete group.";
      alert(msg);
    }
  };

  return (
    <>
      <div style={styles.card}>
        <div style={styles.top}>
          <h3 style={styles.name}>{name}</h3>
          {onDeleted && (
            <button
              style={styles.deleteBtn}
              onClick={() => setShowConfirm(true)}
              title="Delete group"
            >
              🗑
            </button>
          )}
        </div>

        <button
          style={styles.openBtn}
          onClick={() => navigate(`/groups/${id}`)}
        >
          Open Group
        </button>
      </div>

      {/* ✅ Custom confirm modal */}
      {showConfirm && (
        <div style={styles.overlay}>
          <div style={styles.modal}>

            {/* Icon */}
            <div style={styles.iconWrap}>
              <span style={styles.icon}>🗑</span>
            </div>

            <h3 style={styles.modalTitle}>Delete Group</h3>
            <p style={styles.modalMsg}>
              Are you sure you want to delete{" "}
              <strong>"{name}"</strong>?<br />
              <span style={styles.warning}>This action cannot be undone.</span>
            </p>

            <div style={styles.btnRow}>
              <button
                style={styles.cancelBtn}
                onClick={() => setShowConfirm(false)}
                disabled={deleting}
              >
                Cancel
              </button>
              <button
                style={{ ...styles.confirmBtn, opacity: deleting ? 0.7 : 1 }}
                onClick={handleDelete}
                disabled={deleting}
              >
                {deleting ? "Deleting..." : "Yes, Delete"}
              </button>
            </div>

          </div>
        </div>
      )}
    </>
  );
}

export default GroupCard;

const styles = {
  // ── CARD ──
  card: {
    background: "#ffffff",
    padding: "20px",
    borderRadius: "14px",
    boxShadow: "0 2px 12px rgba(0,0,0,0.07)",
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
    minHeight: "130px",
    transition: "box-shadow 0.2s",
  },
  top: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "flex-start",
  },
  name: {
    fontSize: "17px",
    fontWeight: "600",
    color: "#1a1a1a",
    flex: 1,
    marginRight: "8px",
  },
  deleteBtn: {
    background: "#fff0f0",
    color: "#c0392b",
    border: "none",
    width: "30px",
    height: "30px",
    borderRadius: "8px",
    cursor: "pointer",
    fontSize: "14px",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    flexShrink: 0,
    transition: "background 0.15s",
  },
  openBtn: {
    marginTop: "16px",
    background: "#16a085",
    color: "#fff",
    border: "none",
    padding: "10px",
    borderRadius: "8px",
    cursor: "pointer",
    fontWeight: "600",
    width: "100%",
    fontSize: "14px",
  },

  // ── MODAL ──
  overlay: {
    position: "fixed",
    top: 0, left: 0,
    width: "100%", height: "100%",
    background: "rgba(0,0,0,0.45)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    zIndex: 1000,
    backdropFilter: "blur(2px)",
  },
  modal: {
    background: "#fff",
    borderRadius: "18px",
    padding: "32px 28px 24px",
    width: "360px",
    textAlign: "center",
    boxShadow: "0 20px 60px rgba(0,0,0,0.2)",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    gap: "8px",
  },
  iconWrap: {
    width: "60px",
    height: "60px",
    borderRadius: "50%",
    background: "#fff0f0",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    marginBottom: "4px",
  },
  icon: {
    fontSize: "26px",
  },
  modalTitle: {
    fontSize: "18px",
    fontWeight: "700",
    color: "#1a1a1a",
    margin: 0,
  },
  modalMsg: {
    fontSize: "14px",
    color: "#555",
    lineHeight: "1.6",
    margin: "4px 0 12px",
  },
  warning: {
    color: "#c0392b",
    fontSize: "13px",
    fontWeight: "500",
  },
  btnRow: {
    display: "flex",
    gap: "10px",
    width: "100%",
    marginTop: "4px",
  },
  cancelBtn: {
    flex: 1,
    padding: "11px",
    borderRadius: "8px",
    border: "1px solid #e0e0e0",
    background: "#f5f5f5",
    color: "#555",
    fontSize: "14px",
    fontWeight: "500",
    cursor: "pointer",
  },
  confirmBtn: {
    flex: 1,
    padding: "11px",
    borderRadius: "8px",
    border: "none",
    background: "#e74c3c",
    color: "#fff",
    fontSize: "14px",
    fontWeight: "600",
    cursor: "pointer",
  },
};
