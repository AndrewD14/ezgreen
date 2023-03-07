import React from 'react';
import './App.css';
import Home from './features/home/Home';
import logo from './logo.svg';

function App() {

   return (
      <div className="App">
         <header className="App-header">
            <img src={logo} className="App-logo" alt="logo" />
         </header>
         <Home />
      </div>
  );
}

export default App;
