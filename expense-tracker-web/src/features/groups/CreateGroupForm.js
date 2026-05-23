import { useState } from "react";
import { createGroup } from "../../services/groupService";
import axios from "axios";

function CreateGroupForm({ onSuccess }) {

  const [groupName, setGroupName] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!groupName.trim()) {
      alert("Enter group name");
      return;
    }

    try {
      setLoading(true);

      const user = JSON.parse(localStorage.getItem("user"));

      if (!user || !user.id) {
        alert("User not found");
        return;
      }

      // ✅ CREATE GROUP
      const res = await createGroup({
        name: groupName.trim()
      });

      const groupId = res?.data?.id;

      if (!groupId) {
        throw new Error("Invalid group ID");
      }

      // ✅ LINK USER TO GROUP (USE AXIOS FOR STABILITY)
      await axios.post("http://localhost:8080/api/v1/group-members", {
        groupId: groupId,
        userId: user.id
      });

      setGroupName("");

      if (onSuccess) onSuccess();

    } catch (err) {
      console.error("Create group error:", err);
      alert(err?.response?.data?.message || "Failed to create group");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ display: "flex", gap: "10px" }}>

      <input
        placeholder="Group name"
        value={groupName}
        onChange={(e) => setGroupName(e.target.value)}
        disabled={loading}
      />

      <button type="submit" disabled={loading}>
        {loading ? "Creating..." : "Create"}
      </button>

    </form>
  );
}

export default CreateGroupForm;