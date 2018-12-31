# Cyber Security Base - Course Project I

**Task:** In the first course project, your task is to create a web application that has at least five different flaws from the OWASP top ten list (https://www.owasp.org/images/7/72/OWASP_Top_10-2017_%28en%29.pdf.pdf). Starter code for the project is provided on Github at https://github.com/cybersecuritybase/cybersecuritybase-project.

These flaws have been implemented to the software:
* A2:2017-Broken Authentication
* A3:2017-Sensitive Data Exposure
* A5:2017-Broken Access Control
* A6:2017-Security Misconfiguration
* A7:2017-Cross-Site Scripting (XSS)
* A10:2017-Insufficient Logging & Monitoring

---
## Flaw A2:2017-Broken Authentication

**Issue:** Application permits brute force or other automated attacks when trying to login.

Steps to reproduce:
1. Start the cybersecuritybase-project application in Netbeans.
2. Open OWASP ZAP or similar program. Version 2.7.0 was used.
3. Set "URL to attack" to field http://localhost:8080
4. After attacks have started bombarding the site, you can stop the attack.
5. Right click from the left panel: Sites -> http://localhost:8080 -> POST:login(_csrf,password...
6. and select: Attack -> Fuzz
7. Set username to **mike** by highlighting **ZAP** and clicking **Add**, Type is Strings
9. Download password list: https://raw.githubusercontent.com/danielmiessler/SecLists/master/Passwords/darkweb2017-top1000.txt
10. Add the password list to be fuzzed by selecting **ZAP** of password and click **Add**, Type is File
11. Select from Options tab "Follow Redirects" checkbox
12. Start Fuzzer
13. Check from column "Size Resp.Body" which password has unique size. qwertyuiop password should be the only one with size 535 bytes, which means in this case that the password has been found.

### FIX:
Enable feature that failed login attempts are registered and if there are over 3 attempts account gets locked.

Make changes in file:

* Path in NetBeans:
cybersecurity-base-project -> Source Packages -> sec.project.domain -> Siteuser.java
* Or file path:
cybersecuritybase-project\src\main\java\sec\project\domain\Siteuser.java

Uncomment line 64  in `addLoginAttempts()` method:
 ```java
 this.loginAttempts++;
 ```

A feature to unlock user has not been implemented, so the at the moment application needs to be restarted to get account unlocked. You can try fuzzing again. Now the "Size Resp. Body" should be 165 bytes and not differiante except the first one is 0 bytes.

### Second issue with broken authentication
Issue: Application permits default, weak, or well-known passwords, such as "Password1" or "admin/admin“.

Steps to reproduce:
1. Go to http://localhost:8080 with browser and login with admin account: admin/1234
2. Select "Create User" from Site Menu
3. Try to create user with credentials asdf/asdf
4. User is created.

### FIX:
There is JavaScript that enforces to use strong passwords when creating a new user, but forms need to be modified.

Make changes in file:
* Path in NetBeans:
cybersecuritybase-project -> Other Sources -> src/main/resources -> templates -> create-user.html
* Or file path:
cybersecuritybase-project\src\main\resources\templates\create-user.html

Delete or comment this form away (lines from 90 to 101):
```html
        <form action="#" th:action="@{/create-user}" method="POST">
            <p>
                <label for="username">Username</label>: <input type="text" name="username" id="username"/>
            </p>
            <p>
                <label for="psw">Password</label>: <input type="password" name="psw" id="psw"/>
            </p>
            <p>
                <label for="admin">Admin</label>: <input type="checkbox" name="admin" id="admin"/>
            </p>
            <p><input type="submit" value="Submit" /></p>
        </form>
```
Uncomment the second form and message div:
```html
<div class="container">
            <form action="#" th:action="@{/create-user}" method="POST">
                <p>
                    <label for="username">Username</label>:
                    <input type="text" id="username" name="username" required="true"/>
                </p>
                <p>
                    <label for="psw">Password</label>
                    <input type="password" id="psw" name="psw" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" title="Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters" required="true"/>
                </p>
                <p>
                    <input type="submit" value="Submit"/>
                </p>
            </form>
        </div>

<div id="message">
    <h3>Password must contain the following:</h3>
    <p id="letter" class="invalid">A <b>lowercase</b> letter</p>
    <p id="capital" class="invalid">A <b>capital (uppercase)</b> letter</p>
    <p id="number" class="invalid">A <b>number</b></p>
    <p id="length" class="invalid">Minimum <b>8 characters</b></p>
</div>
```

**The whole application might need restart in Netbeans, so that the changes start workgin!**

**Try with ctrl+F5 to refresh the browser if new information "Password must contain..." does not show up.**

Now the form should not accept password which is not strong enough. The definition of strong password is given to user on the page.

---
## Flaws A3:2017-Sensitive Data Exposure & A5:2017-Broken Access Control

Issue: Sensitive data of name and address are freely available to anyone browsing to that address.

Steps to reproduce:
1. Login with browser using admin credentials (admin/1234) to http://localhost:8080/
2. Click Signup from Site Menu
3. Add name and address
4. Logout
5. Go to http://localhost:8080/registered
6. Anybody is able to see information about signed up users.

### FIX:
This application has user authorities/roles of USER and ADMIN. Only administrators/ADMIN should have access to the list of registered users.

Make changes in file:
* Path in NetBeans:
cybersecurity-base-project -> Source Packages -> sec.project.config -> SecurityConfiguration.java
* Or file path:
cybersecuritybase-project\src\main\java\sec\project\config\SecurityConfiguration.java

Comment line 31:
```java
.antMatchers("/registered").permitAll()
```
Uncomment line 32:
```java
.antMatchers("/registered").hasAuthority("ADMIN")
```

Now only a user which has authority/role of ADMIN can access the /registered page.

---
## Flaws A6:2017-Security Misconfiguration and A7:2017-Cross-Site Scripting (XSS)

Issue: It is possible to get sessionID and inject JavaScript

Steps to reproduce:
1. Login with browser using admin credentials (admin/1234) to http://localhost:8080/
2. Click Signup from Site Menu
3. Add name and as address add:
```javascript
<script>alert(document.cookie);</script>
```
4. Go to http://localhost:8080/registered
5. There should be a JavaScript alert which shows sessionID

### FIX:
Make changes in file:
* Path in NetBeans:
cybersecurity-base-project -> Source Packages -> sec.project -> CyberSecurityBaseProjectApplication.java
* Or file path:
cybersecuritybase-project\src\main\java\sec\project\CyberSecurityBaseProjectApplication.java

Enable HttpOnly cookies by commenting line 14 away and restart browser, so that the HttpOnly is set on.

Line 24: `cntxt.setUseHttpOnly(false);`

By enabling ```UseHttpOnly``` cookies, ```<script>alert(document.cookie);</script>``` does not alert sessionID anymore, but running script is still possible, so we need to do the following.

Disable unescaped user input by:

Make changes in file:
* Path in NetBeans:
cybersecurity-base-project -> Other Sources -> src/main/resources -> templates -> registered.html
* Or file path:
cybersecuritybase-project\src\main\resources\templates\registered.html

Replace 'utext' with 'text' on line 15:
```html
<li th:each="person : ${registered}" th:utext="${person.name} + ' ' + ${person.address}">Participant</li>
```

---
## Flaw A10:2017-Insufficient Logging & Monitoring

Issue: Failed login attempts are not logged. Without logging the attempts it is hard to trace if some accounts are tried to be brute forced.

Steps to reproduce issue:
1. Login with browser using any credentials to http://localhost:8080/
2. Check Output of logs. The last line should be ```Using ASTQueryTranslatorFactory```, and there is no good information about failed login attempt.

### FIX:
Make changes in file:
* Path in NetBeans:
cybersecuritybase-project -> Source Packages -> sec.project.config -> AuthenticationEventListener.java
* Or file path:
cybersecuritybase-project\src\main\java\sec\project\config\AuthenticationEventListener.java

Uncomment line 35:
```java
logger.info("AuthenticationFailed, username: " + username + ", failed login attempts: " + user.getLoginAttempts());
```

Uncomment line39:
```java
logger.info("AuthenticationFailed, username does not exist: " + username);
```

Now there is more information if somebody is trying to brute force accounts. Of course more information could be saved, for example IP address and block the address if there are too many attempts.
