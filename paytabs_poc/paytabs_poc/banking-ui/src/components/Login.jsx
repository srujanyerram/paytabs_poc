import React, {useState} from 'react';

export default function Login({onLogin}){
  const [user,setUser]=useState('');
  const [pass,setPass]=useState('');

  const submit=()=>{
    // simple hardcoded auth for POC
    if(user==='admin' && pass==='admin') onLogin({username:'admin',role:'ADMIN'});
    else if(user==='cust1' && pass==='pass') onLogin({username:'cust1',role:'CUSTOMER',cardNumber:'4123456789012345'});
    else alert('Invalid credentials. Use admin/admin or cust1/pass');
  }

  return (
    <div className="app card">
      <h2>Login</h2>
      <input placeholder="username" value={user} onChange={e=>setUser(e.target.value)} /><br/><br/>
      <input placeholder="password" value={pass} onChange={e=>setPass(e.target.value)} type="password" /><br/><br/>
      <button className="btn" onClick={submit}>Login</button>
    </div>
  )
}
