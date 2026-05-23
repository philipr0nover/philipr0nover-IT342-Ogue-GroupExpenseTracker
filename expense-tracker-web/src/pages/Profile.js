import Sidebar from "../components/Sidebar";
import HeaderBar from "../components/HeaderBar";

function Profile() {

  let user = null;

  try {
    user = JSON.parse(localStorage.getItem("user"));
  } catch {
    user = null;
  }

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

        {/* SAME HEADER STYLE */}
        <HeaderBar title="Profile" />

        {/* SUBTEXT (same tone as dashboard sections) */}
        <p style={{ color: "#777", marginTop: "5px" }}>
          Manage your account information
        </p>

        {/* GRID LIKE DASHBOARD */}
        <div
          style={{
            display: "grid",
            gridTemplateColumns: "300px 1fr",
            gap: "20px",
            marginTop: "20px"
          }}
        >

          {/* LEFT CARD (same as StatCard wrapper) */}
          <div style={cardWrapper}>

            <div style={{ textAlign: "center", padding: "20px" }}>

              <div
                style={{
                  width: "80px",
                  height: "80px",
                  borderRadius: "50%",
                  background: "#16a085",
                  color: "white",
                  fontSize: "32px",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  margin: "0 auto 10px"
                }}
              >
                {user?.email
                  ? user.email.charAt(0).toUpperCase()
                  : "U"}
              </div>

              <h3 style={{ marginBottom: "5px" }}>
                {user?.firstname || "User"} {user?.lastname || ""}
              </h3>

              <p style={{ color: "#888", fontSize: "14px" }}>
                Expense Tracker Member
              </p>

              <button
                style={{
                  marginTop: "12px",
                  padding: "10px 15px",
                  borderRadius: "8px",
                  border: "none",
                  background: "#16a085",
                  color: "white",
                  cursor: "pointer",
                  fontWeight: "600"
                }}
              >
                Edit Profile
              </button>

            </div>

          </div>

          {/* RIGHT CARD */}
          <div style={cardWrapper}>

            <h3 style={{ marginBottom: "15px" }}>
              Account Information
            </h3>

            {[
              { label: "Email", value: user?.email },
              { label: "First Name", value: user?.firstname },
              { label: "Last Name", value: user?.lastname },
              { label: "Role", value: user?.role || "USER" }
            ].map((item, index) => (
              <div
                key={index}
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  padding: "12px 0",
                  borderBottom: "1px solid #eee"
                }}
              >
                <span style={{ color: "#888" }}>
                  {item.label}
                </span>

                <span style={{ fontWeight: "600" }}>
                  {item.value || "—"}
                </span>
              </div>
            ))}

          </div>

        </div>

      </div>

    </div>
  );
}

export default Profile;

const cardWrapper = {
  background: "white",
  padding: "20px",
  borderRadius: "12px",
  boxShadow: "0 2px 10px rgba(0,0,0,0.05)"
};