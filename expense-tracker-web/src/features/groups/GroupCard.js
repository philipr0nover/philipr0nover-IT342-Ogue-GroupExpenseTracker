import { useNavigate } from "react-router-dom";

function GroupCard({ id, name }) {

  const navigate = useNavigate();

  return (

    <div style={styles.card}>

      <h3 style={styles.name}>{name}</h3>

      <button
        style={styles.button}
        onClick={() => navigate(`/groups/${id}`)}
      >
        Open Group
      </button>

    </div>

  );
}

export default GroupCard;

const styles = {
  card: {
    background: "#ffffff",
    padding: "20px",
    borderRadius: "12px",
    boxShadow: "0 2px 10px rgba(0,0,0,0.05)",
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
    minHeight: "120px"
  },

  name: {
    fontSize: "18px",
    fontWeight: "600"
  },

  button: {
    marginTop: "15px",
    background: "#16a085",
    color: "#fff",
    border: "none",
    padding: "10px",
    borderRadius: "8px",
    cursor: "pointer",
    fontWeight: "600",
    width: "100%"
  }
};