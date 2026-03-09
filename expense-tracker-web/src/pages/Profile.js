import Sidebar from "../components/Sidebar";
import ProfileCard from "../components/ProfileCard";
import ProfileInfo from "../components/ProfileInfo";

function Profile(){

  return(

    <div className="dashboard-container">

      <Sidebar/>

      <div className="dashboard-main">

        <h1>Profile</h1>

        <div className="profile-container">

          <ProfileCard/>

          <ProfileInfo/>

        </div>

      </div>

    </div>

  );
}

export default Profile;