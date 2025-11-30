import React, {useState} from 'react';
import Login from './components/Login';
import CustomerDashboard from './pages/CustomerDashboard';
import AdminDashboard from './pages/AdminDashboard';

export default function App(){
  const [user, setUser] = useState(null);

  if(!user) return <Login onLogin={setUser} />
  return user.role === 'ADMIN' ? <AdminDashboard user={user} /> : <CustomerDashboard user={user} />
}
