import React, {useEffect, useState} from 'react';
import axios from 'axios';

export default function CustomerDashboard({user}) {
  const [transactions, setTransactions] = useState([]);
  const [balance, setBalance] = useState(null);
  const [amount, setAmount] = useState('');

  useEffect(()=>{
    fetchData();
  },[]);

  async function fetchData(){
    try{
      const res = await axios.get('http://localhost:8082/process/card/' + user.cardNumber);
      setTransactions(res.data);
      // derive balance from last known card by hitting system2 card lookup (not implemented here)
      // simple approach: sum topups - withdraws from logs
      let bal = 0;
      if(res.data && res.data.length) {
        // find last successful tx and adjust not implemented; use seeded balance assumption
      }
    }catch(e){ console.error(e); }
  }

  async function topup(){
    try{
      const payload = { cardNumber: user.cardNumber, pin: '1234', amount: parseFloat(amount), type: 'topup' };
      const r = await axios.post('http://localhost:8081/transaction', payload);
      alert(JSON.stringify(r.data));
      fetchData();
    }catch(e){ alert('Error: ' + e.message) }
  }

  return (
    <div className="app">
      <header><h2>Customer Dashboard</h2><div>Welcome {user.username}</div></header>
      <div className="card">
        <h3>Top-up</h3>
        <input placeholder="amount" value={amount} onChange={e=>setAmount(e.target.value)} />
        <button className="btn" onClick={topup}>Top Up</button>
      </div>

      <div>
        <h3>Transactions</h3>
        {transactions.map(t=>(
          <div key={t.id} className="card">
            <div>{t.type} — {t.amount} — {t.status}</div>
            <div style={{color:'#6b7280'}}>{t.timestamp}</div>
          </div>
        ))}
      </div>
    </div>
  )
}
