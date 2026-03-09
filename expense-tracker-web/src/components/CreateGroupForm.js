import { useState } from "react";

function CreateGroupForm(){

  const [groupName,setGroupName] = useState("");

  const createGroup = (e)=>{

    e.preventDefault();

    if(groupName===""){
      alert("Enter group name");
      return;
    }

    alert("Group created: " + groupName);

    setGroupName("");

  };

  return(

    <form className="create-group-form" onSubmit={createGroup}>

      <input
        className="group-input"
        placeholder="Enter group name"
        value={groupName}
        onChange={(e)=>setGroupName(e.target.value)}
      />

      <button className="group-create-btn">
        Create Group
      </button>

    </form>

  );

}

export default CreateGroupForm;