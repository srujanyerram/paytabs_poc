import React, {useEffect, useState} from 'react';
import axios from 'axios';

export default function AdminDashboard({user}) {
  const [transactions, setTransactions] = useState([]);

  useEffect(()=>{ fetchAll() },[]);

  async function fetchAll(){
    try{
      const res = await axios.get('http://localhost:8082/process/all');
      setTransactions(res.data);
    }catch(e){ console.error(e) }
  }

  return (
    <div className="app">
      <header><h2>Admin Dashboard</h2><div>{user.username}</div></header>
      <button className="btn" onClick={fetchAll}>Refresh</button>
      <div>
        {transactions.map(t=>(
          <div key={t.id} className="card">
            <div>{t.cardNumber} — {t.type} — {t.amount} — {t.status}</div>
            <div style={{color:'#6b7280'}}>{t.timestamp}</div>
          </div>
        ))}
      </div>
    </div>
  )
}
