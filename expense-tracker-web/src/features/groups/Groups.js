import { useEffect, useState } from "react";
import Sidebar from "../../components/Sidebar";
import HeaderBar from "../../components/HeaderBar";
import GroupCard from "./GroupCard";
import CreateGroupForm from "./CreateGroupForm";
import { getGroups } from "../../services/groupService";

function Groups(){

  const [groups, setGroups] = useState([]);

  useEffect(() => {
    fetchGroups();
  }, []);

  const fetchGroups = async () => {
    try {
      const res = await getGroups();
      setGroups(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  return(
    <div className="dashboard-container">

      <Sidebar/>

      <div className="dashboard-main">

        <HeaderBar title="Groups"/>

        <h1>Groups</h1>

        <CreateGroupForm onSuccess={fetchGroups}/>

        <div className="group-container">

          {groups.map((group) => (
            <GroupCard
              key={group.id}
              id={group.id}
              name={group.name}
              members={group.members}
            />
          ))}

        </div>

      </div>

    </div>
  );
}

export default Groups;