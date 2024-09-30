import React from "react";
import ReactDOM from "react-dom";
import App from "./App";
import {BrowserRouter, Routes, Route} from "react-router-dom";
import MultiMode from "./pages/MultiMod";
import SingleMod from "./pages/SingleMod";

const rootElement = document.getElementById("root");
ReactDOM.render(
  <BrowserRouter>
    <Routes>
        <Route path="/" element={<App />} />
        <Route path="/single" element={<SingleMod />} />
        <Route path="/multi" element={<MultiMode />} />
    </Routes>
  </BrowserRouter>,
  rootElement
);
