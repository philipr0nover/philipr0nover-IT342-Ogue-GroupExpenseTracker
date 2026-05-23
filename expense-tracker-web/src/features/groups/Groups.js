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

      const user = JSON.parse(localStorage.getItem("user"));

      if (!user || !user.id) {
        setGroups([]);
        setLoading(false);
        return;
      }

      const res = await getGroups(user.id);

      setGroups(Array.isArray(res.data) ? res.data : []);

    } catch (err) {
      console.error("Fetch groups error:", err);
      setGroups([]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ display: "flex" }}>

      <Sidebar />

      <div
        style={{
          flex: 1,
          padding: "30px",
          background: "#f5f6fa",
          minHeight: "100vh"
        }}
      >

        <HeaderBar title="Groups" />

        <p style={{ color: "#777", marginTop: "5px" }}>
          Manage your groups
        </p>

        <div style={{ marginTop: "20px" }}>
          <CreateGroupForm onSuccess={fetchGroups} />
        </div>

        <div
          style={{
            marginTop: "20px",
            display: "grid",
            gridTemplateColumns: "repeat(auto-fill, minmax(250px, 1fr))",
            gap: "20px"
          }}
        >

          {loading ? (
            <p>Loading...</p>
          ) : groups.length === 0 ? (
            <p>No groups yet</p>
          ) : (
            groups.map((group) => (
              <GroupCard
                key={group.id}
                id={group.id}
                name={group.name}
                members={group.members?.length || 0}
              />
            ))
          )}

        </div>

      </div>

    </div>
  );
}

export default Groups;