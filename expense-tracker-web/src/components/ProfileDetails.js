function ProfileDetails(){

  const user = localStorage.getItem("user");

  return(

    <div className="profile-info">

      <h2>Account Information</h2>

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

      <div className="profile-row">
        <span>Member Since</span>
        <span>2026</span>
      </div>

    </div>

  );

}

export default ProfileDetails;