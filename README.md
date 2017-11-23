rbac-man
========

This project provides an implementation of the PEP/PDP/PIP/PAP access control pattern. The implementation is in Java. For the moment it implements an XACML PDP. It provides a web based user interface to manage users, roles, actions, assign actions to roles, and assign users to roles.

Architecture
------------

![rbac-man architecture](https://raw.githubusercontent.com/cetic/rbac-man/master/doc/img/rbac-man-archi.png "rbac-man architecture")


Interface
---------

rbac-man offers a management interface with CRUD operations on its entities (user, rule, actions, ...), rule testing and logs.

![Demo video](rbacman.gif)


![rbac-man test interface](https://raw.githubusercontent.com/cetic/rbac-man/master/doc/img/rbac-man-screenshot-test-interface.png "rbac-man test interface")

Sequence diagram
----------------

![rbac-man sequence diagram](https://raw.githubusercontent.com/cetic/rbac-man/master/doc/img/rbac-man-access_control_sequence_diagram.png "rbac-man sequence diagram")

Secure Logger
-------------

![rbac-man secure logger](https://raw.githubusercontent.com/cetic/rbac-man/master/doc/img/rbac-man-securelogger.png "rbac-man secure logger")

### Log ontology

![rbac-man log ontology](https://raw.githubusercontent.com/cetic/rbac-man/master/doc/img/rbac-man-log-ontology.png "rbac-man log ontology")

Notes
-----

Draw.io sources can be found in doc/ folder
