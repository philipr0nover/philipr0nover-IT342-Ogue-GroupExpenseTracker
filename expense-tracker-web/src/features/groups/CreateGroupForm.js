import { useState } from "react";
import { createGroup } from "../../services/groupService";

function CreateGroupForm({ onSuccess }){

  const [groupName, setGroupName] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!groupName) {
      alert("Enter group name");
      return;
    }

    try {
      await createGroup({
        name: groupName,
        members: 1
      });

      setGroupName("");

      if (onSuccess) onSuccess();

    } catch (err) {
      console.error(err);
      alert("Failed to create group");
    }
  };

  return(
    <form onSubmit={handleSubmit}>

      <input
        placeholder="Group name"
        value={groupName}
        onChange={(e)=>setGroupName(e.target.value)}
      />

      <button>Create</button>

    </form>
  );
}

export default CreateGroupForm;