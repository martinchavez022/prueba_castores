import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import LoginPage from './components/LoginPage';
import MainPage from './components/MainPage';
import ProtectedRoute from './components/ProtectedRoute';
import InventoryPage from './components/InventoryPage';
import HistoryPage from './components/HistoryPage';
import SubtractPage from './components/SubtractPage';
import './App.css';

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route element={<ProtectedRoute />}>
            <Route path="/" element={<MainPage />} />
            <Route path="/inventory" element={<InventoryPage />} />
            <Route path="/history" element={<HistoryPage />} />
            <Route path="/subtract" element={<SubtractPage />} />
          </Route>
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
