import { useState } from "react";
import { createGroup } from "../../services/groupService";

function CreateGroupForm({ onSuccess }) {

  const [groupName, setGroupName] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!groupName.trim()) {
      alert("Enter group name");
      return;
    }

    try {
      const user = JSON.parse(localStorage.getItem("user"));

      const res = await createGroup({
        name: groupName.trim()
      });

      const groupId = res.data.id;

      await fetch("http://localhost:8080/api/v1/group-members", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          groupId,
          userId: user.id
        })
      });

      setGroupName("");
      if (onSuccess) onSuccess();

    } catch (err) {
      console.error(err);
      alert("Failed to create group");
    }
  };

  return (
    <div style={styles.container}>
      <form onSubmit={handleSubmit} style={styles.form}>

        <input
          placeholder="Enter group name..."
          value={groupName}
          onChange={(e) => setGroupName(e.target.value)}
          style={styles.input}
        />

        <button type="submit" style={styles.button}>
          Create
        </button>

      </form>
    </div>
  );
}

export default CreateGroupForm;

const styles = {
  container: {
    maxWidth: "600px", // ✅ NOT full width anymore
    marginTop: "10px"
  },

  form: {
    display: "flex",
    gap: "10px",
    background: "#ffffff",
    padding: "10px",
    borderRadius: "12px",
    border: "1px solid #eee",
    boxShadow: "0 1px 4px rgba(0,0,0,0.04)"
  },

  input: {
    flex: 1,
    padding: "10px",
    borderRadius: "8px",
    border: "1px solid #ddd",
    fontSize: "14px",
    outline: "none"
  },

  button: {
    background: "#16a085",
    color: "white",
    border: "none",
    padding: "10px 18px",
    borderRadius: "8px",
    cursor: "pointer",
    fontWeight: "600",
    whiteSpace: "nowrap"
  }
};