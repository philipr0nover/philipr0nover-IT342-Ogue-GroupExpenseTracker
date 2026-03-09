function ProfileInfo(){

  const user = localStorage.getItem("user");

  return(

    <div className="profile-info">

      <h3>User Information</h3>

      <div className="profile-row">
        <span>Email</span>
        <span>{user}</span>
      </div>

      <div className="profile-row">
        <span>Account Type</span>
        <span>Standard User</span>
      </div>

      <div className="profile-row">
        <span>Status</span>
        <span>Active</span>
      </div>

    </div>

  );
}

export default ProfileInfo;