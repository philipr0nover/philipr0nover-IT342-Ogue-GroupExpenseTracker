import Sidebar from "../components/Sidebar";
import ProfileCard from "../components/ProfileCard";
import ProfileDetails from "../components/ProfileDetails";

function Profile(){

  return(

    <div className="dashboard-container">

      <Sidebar/>

      <div className="dashboard-main">

        <div className="header-bar">
          <h1>Profile</h1>
        </div>

        <div className="profile-container">

          <ProfileCard/>

          <ProfileDetails/>

        </div>

      </div>

    </div>

  );

}

export default Profile;