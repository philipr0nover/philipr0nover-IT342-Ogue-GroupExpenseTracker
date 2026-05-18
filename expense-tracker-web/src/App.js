import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from "./features/auth/Login";
import Register from "./features/auth/Register";
import Dashboard from "./pages/Dashboard";
import Profile from "./pages/Profile";

// ✅ FIXED IMPORT
import Groups from "./features/groups/Groups";
import GroupDetails from "./features/groups/GroupDetails";

import Expenses from "./features/expenses/Expenses";

import ProtectedRoute from "./components/ProtectedRoute";

function App(){

  return(

    <BrowserRouter>

      <Routes>

        <Route path="/" element={<Login />} />

        <Route path="/register" element={<Register />} />

        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <Dashboard/>
            </ProtectedRoute>
          }
        />

        <Route
          path="/profile"
          element={
            <ProtectedRoute>
              <Profile/>
            </ProtectedRoute>
          }
        />

        {/* ✅ GROUPS PAGE */}
        <Route
          path="/groups"
          element={
            <ProtectedRoute>
              <Groups/>
            </ProtectedRoute>
          }
        />

        {/* 🔥 NEW ROUTE (IMPORTANT) */}
        <Route
          path="/groups/:id"
          element={
            <ProtectedRoute>
              <GroupDetails/>
            </ProtectedRoute>
          }
        />

        {/* EXPENSES PAGE */}
        <Route
          path="/expenses"
          element={
            <ProtectedRoute>
              <Expenses/>
            </ProtectedRoute>
          }
        />

      </Routes>

    </BrowserRouter>

  );
}

export default App;