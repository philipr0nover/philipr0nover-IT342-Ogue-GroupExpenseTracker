import Sidebar from "../components/Sidebar";
import HeaderBar from "../components/HeaderBar";
import GroupCard from "../components/GroupCard";
import CreateGroupForm from "../components/CreateGroupForm";

function Groups(){

  return(

    <div className="dashboard-container">

      <Sidebar/>

      <div className="dashboard-main">

        <HeaderBar/>

        <h1>Groups</h1>

        <CreateGroupForm/>

        <div className="group-container">

          <GroupCard name="Trip to Cebu" members={4}/>
          <GroupCard name="Apartment Bills" members={3}/>
          <GroupCard name="Food Buddies" members={5}/>

        </div>

      </div>

    </div>

  );

}

export default Groups;