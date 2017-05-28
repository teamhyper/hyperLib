<!doctype html>
<html>
  <head>
    <title>HYPERlib</title>
    <style>
      body {
      margin: 40px auto;
      max-width: 650px;
      line-height: 1.4;
      font-size: 18px;
      padding: 0 10px;
      color: #222;
      }
    </style>
  </head>
  <body>
    <h1>HYPERlib</h1>
    <p>HYPERlib is Team #69 HYPER's library for FRC. This library
    contains features we re-use each year on top of what WPILib
    provides.</p>
    <p>James doesn't do websites, so when nobody else does it, he
      builds it with shell commands and m4.</p>
    <h2>Documentation</h2>
    <h3>Releases</h3>
    <ul>
m4_esyscmd(for f in doc/release/*; do echo "<li><a href=\"$f\">$(basename $f)</a></li>"; done)
    </ul>
    <h3>Latest Git Builds</h3>
    <ul>
m4_esyscmd(for f in doc/latest/*; do echo "<li><a href=\"$f\">$(basename $f)</a></li>"; done)
    </ul>
    <h2>Links</h2>
    <ul>
      <li><a href="https://github.com/teamhyper/hyperlib">Github page</a></li>
      <li><a href="http://hyperonline.org">Team HYPER Website</a></li>
    </ul>
  </body>
</html>
