import { useEffect, useState } from "react";
import Sidebar from "../../components/Sidebar";
import HeaderBar from "../../components/HeaderBar";
import GroupCard from "./GroupCard";
import CreateGroupForm from "./CreateGroupForm";
import { getGroups } from "../../services/groupService";

function Groups() {

  const [groups, setGroups] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchGroups();
  }, []);

  const fetchGroups = async () => {
    try {
      setLoading(true);

      const userData = localStorage.getItem("user");
      if (!userData) {
        setGroups([]);
        return;
      }

      const user = JSON.parse(userData);

      if (!user?.id) {
        setGroups([]);
        return;
      }

      const res = await getGroups(user.id);

      setGroups(res.data || []);

    } catch (err) {
      console.error("Fetch groups error:", err);
      setGroups([]); // ✅ prevent UI crash
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ display: "flex" }}>

      <Sidebar />

      <div style={styles.container}>

        <HeaderBar title="Groups" />

        <p style={styles.subtitle}>
          Manage your groups
        </p>

        {/* CREATE */}
        <div style={styles.formWrapper}>
          <CreateGroupForm onSuccess={fetchGroups} />
        </div>

        {/* GROUP LIST */}
        <div style={styles.grid}>

          {loading ? (
            <p style={styles.empty}>Loading...</p>
          ) : groups.length === 0 ? (
            <p style={styles.empty}>No groups yet</p>
          ) : (
            groups.map((group) => (
              <GroupCard
                key={group.id}
                id={group.id}
                name={group.name}
              />
            ))
          )}

        </div>

      </div>

    </div>
  );
}

export default Groups;

const styles = {
  container: {
    flex: 1,
    padding: "30px",
    background: "#f5f6fa",
    minHeight: "100vh"
  },

  subtitle: {
    color: "#777",
    marginTop: "5px",
    marginBottom: "10px"
  },

  formWrapper: {
    marginTop: "10px",
    marginBottom: "20px"
  },

  grid: {
    display: "grid",
    gridTemplateColumns: "repeat(auto-fill, minmax(250px, 1fr))",
    gap: "20px"
  },

  empty: {
    color: "#777",
    fontSize: "14px"
  }
};