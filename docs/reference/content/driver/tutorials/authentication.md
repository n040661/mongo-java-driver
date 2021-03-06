+++
date = "2016-05-29T23:27:26-04:00"
title = "Authentication"
[menu.main]
  parent = "Connect to MongoDB"
  identifier = "Authentication"
  weight = 20
  pre = "<i class='fa'></i>"
+++

## Authentication

The Java driver supports all [MongoDB authentication mechanisms]({{<docsref "core/authentication/">}}), including those only available in the
[MongoDB Enterprise Edition]({{<docsref "administration/install-enterprise/">}}).

## `MongoCredential`

```java
import com.mongodb.MongoCredential;
```

New MongoClient API (since 3.7):

```java
import com.mongodb.ConnectionString;
import com.mongodb.clients.MongoClients;
import com.mongodb.clients.MongoClient;
```

Legacy MongoClient API:

```java
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
```

An authentication credential is represented as an instance of the
[`MongoCredential`]({{<apiref "com/mongodb/MongoCredential.html">}}) class. The [`MongoCredential`]({{<apiref "com/mongodb/MongoCredential.html">}}) class includes static
factory methods for each of the supported authentication mechanisms.

You can also use a [`MongoClientURI`]({{< apiref "/com/mongodb/MongoClientURI.html">}}) and pass it to a 
[`MongoClient()`]({{< apiref "com/mongodb/MongoClient.html">}}) constructor that takes a `MongoClientURI` parameter.

## Default Authentication Mechanism

Starting in MongoDB 3.0, MongoDB changed the default authentication
mechanism from [`MONGODB-CR`]({{<docsref "core/security-mongodb-cr">}}) to
[`SCRAM-SHA-1`]({{<docsref "core/security-scram-sha-1">}}).

To create a credential that will authenticate using the default
authentication mechanism regardless of server version, create a
credential using the [`createCredential`]({{<apiref "com/mongodb/MongoCredential.html#createCredential-java.lang.String-java.lang.String-char:A-">}})
static factory method:

```java
String user;     // the user name
String database; // the name of the database in which the user is defined
char[] password; // the password as a character array
// ...
MongoCredential credential = MongoCredential.createCredential(user, database, password);
```

and then construct a MongoClient with that credential.  Using the new (since 3.7) MongoClient API:

```java
MongoClient mongoClient = MongoClients.create(
        MongoClientSettings.builder()
                .applyToClusterSettings(builder -> 
                        builder.hosts(Arrays.asList(new ServerAddress("host1", 27017))))
                .credential(credential)
                .build());
```

or using the legacy MongoClient API:

```java
MongoClient mongoClient = new MongoClient(new ServerAddress("host1", 27017), credential);
```

Or use a connection string without explicitly specifying the authentication mechanism. Using the new MongoClient API:

```java
MongoClient mongoClient = MongoClients.create("mongodb://user1:pwd1@host1/?authSource=db1");
```

or using the legacy MongoClient API:

```java
MongoClientURI uri = new MongoClientURI("mongodb://user1:pwd1@host1/?authSource=db1");
MongoClient mongoClient = new MongoClient(uri);
```

For challenge and response mechanisms, using the default authentication
mechanism is the recommended approach as it will make
upgrading from MongoDB 2.6 to MongoDB 3.0 seamless, even after
[upgrading the authentication schema]({{<docsref "release-notes/3.0-scram/">}}).

## SCRAM-SHA-1

To explicitly create a credential of type [`SCRAM-SHA-1`]({{<docsref "core/security-scram-sha-1/">}}), use the 
[`createScramSha1Credential`]({{<apiref "com/mongodb/MongoCredential.html#createScramSha1Credential-java.lang.String-java.lang.String-char:A-">}}) 
method:

```java
String user;     // the user name
String database; // the name of the database in which the user is defined
char[] password; // the password as a character array
// ...
MongoCredential credential = MongoCredential.createScramSha1Credential(user, database, password);
```

and then construct a MongoClient with that credential.  Using the new (since 3.7) MongoClient API:

```java
MongoClient mongoClient = MongoClients.create(
        MongoClientSettings.builder()
                .applyToClusterSettings(builder -> 
                        builder.hosts(Arrays.asList(new ServerAddress("host1", 27017))))
                .credential(credential)
                .build());
```

or using the legacy MongoClient API:

```java
MongoClient mongoClient = new MongoClient(new ServerAddress("host1", 27017), credential);
```

Or use a connection string that explicitly specifies the `authMechanism=SCRAM-SHA-1`. Using the new MongoClient API:

```java
MongoClient mongoClient = MongoClients.create("mongodb://user1:pwd1@host1/?authSource=db1&authMechanism=SCRAM-SHA-1");
```

or using the legacy MongoClient API:

```java
MongoClientURI uri = new MongoClientURI("mongodb://user1:pwd1@host1/?authSource=db1&authMechanism=SCRAM-SHA-1");
MongoClient mongoClient = new MongoClient(uri);
```

## MONGODB-CR

To explicitly create a credential of type [`MONGODB-CR`]({{<docsref "core/security-mongodb-cr">}}) use the [`createMongCRCredential`]({{<apiref "com/mongodb/MongoCredential.html#createMongoCRCredential-java.lang.String-java.lang.String-char:A-">}})
static factory method:

```java
String user; // the user name
String database; // the name of the database in which the user is defined
char[] password; // the password as a character array
// ...
MongoCredential credential = MongoCredential.createMongoCRCredential(user,
                                                                    database,
                                                                    password);
```

and then construct a MongoClient with that credential.  Using the new (since 3.7) MongoClient API:

```java
MongoClient mongoClient = MongoClients.create(
        MongoClientSettings.builder()
                .applyToClusterSettings(builder -> 
                        builder.hosts(Arrays.asList(new ServerAddress("host1", 27017))))
                .credential(credential)
                .build());
```

or using the legacy MongoClient API:

```java
MongoClient mongoClient = new MongoClient(new ServerAddress("host1", 27017), credential);
```

Or use a connection string that explicitly specifies the `authMechanism=MONGODB-CR`. Using the new MongoClient API:

```java
MongoClient mongoClient = MongoClients.create("mongodb://user1:pwd1@host1/?authSource=db1&authMechanism=MONGODB-CR");
```

or using the legacy MongoClient API:

```java
MongoClientURI uri = new MongoClientURI("mongodb://user1:pwd1@host1/?authSource=db1&authMechanism=MONGODB-CR");
MongoClient mongoClient = new MongoClient(uri);
```

{{% note %}}
After the [authentication schema upgrade]({{<docsref "release-notes/3.0-scram/">}}) from MONGODB-CR to SCRAM-SHA-1,
MONGODB-CR credentials will fail to authenticate.
{{% /note %}}

## X.509

With [X.509]({{<docsref "core/security-x.509">}}) mechanism, MongoDB uses the
X.509 certificate presented during SSL negotiation to
authenticate a user whose name is derived from the distinguished name
of the X.509 certificate.

X.509 authentication requires the use of SSL connections with
certificate validation and is available in MongoDB 2.6 and later. To
create a credential of this type use the
[`createMongoX509Credential`]({{<apiref "com/mongodb/MongoCredential.html#createMongoX509Credential-java.lang.String-">}}) static factory method:

```java
String user;     // The X.509 certificate derived user name, e.g. "CN=user,OU=OrgUnit,O=myOrg,..."
// ...
MongoCredential credential = MongoCredential.createMongoX509Credential(user);
MongoClientOptions options = MongoClientOptions.builder().sslEnabled(true).build();
```

and then construct a MongoClient with that credential.  Using the new (since 3.7) MongoClient API:

```java
MongoClient mongoClient = MongoClients.create(
        MongoClientSettings.builder()
                .applyToClusterSettings(builder -> 
                        builder.hosts(Arrays.asList(new ServerAddress("host1", 27017))))
                .credential(credential)
                .build());
```

or using the legacy MongoClient API:

```java
MongoClient mongoClient = new MongoClient(new ServerAddress("host1", 27017), credential, options);
```

Or use a connection string that explicitly specifies the `authMechanism=MONGODB-X509`. Using the new MongoClient API:
                                                                                      
```java
MongoClient mongoClient = MongoClients.create("mongodb://subjectName@host1/?authMechanism=MONGODB-X509&ssl=true");
```

or using the legacy MongoClient API:

```java
MongoClientURI uri = new MongoClientURI("mongodb://subjectName@host1/?authMechanism=MONGODB-X509&ssl=true");
MongoClient mongoClient = new MongoClient(uri);
```

See the MongoDB server [x.509 tutorial]({{<apiref "tutorial/configure-x509-client-authentication/#add-x-509-certificate-subject-as-a-user">}})
for more information about determining the subject
name from the certificate.

## Kerberos (GSSAPI) {#gssapi}

[MongoDB Enterprise](http://www.mongodb.com/products/mongodb-enterprise) supports proxy
authentication through Kerberos service. To create a credential of type
[Kerberos (GSSAPI)]({{<apiref "core/authentication/#kerberos-authentication">}}), use the
[`createGSSAPICredential`]({{<apiref "com/mongodb/MongoCredential.html#createGSSAPICredential-java.lang.String-">}})
static factory method:

```java
String user;   // The Kerberos user name, including the realm, e.g. "user1@MYREALM.ME"
// ...
MongoCredential credential = MongoCredential.createGSSAPICredential(user);
```

and then construct a MongoClient with that credential.  Using the new (since 3.7) MongoClient API:

```java
MongoClient mongoClient = MongoClients.create(
        MongoClientSettings.builder()
                .applyToClusterSettings(builder -> 
                        builder.hosts(Arrays.asList(new ServerAddress("host1", 27017))))
                .credential(credential)
                .build());
```

or using the legacy MongoClient API:

```java
MongoClient mongoClient = new MongoClient(new ServerAddress("host1", 27017), credential);
```

Or use a connection string that explicitly specifies the `authMechanism=GSSAPI`. Using the new MongoClient API:
                                                                                      
```java
MongoClient mongoClient = MongoClients.create("mongodb://username%40REALM.ME@host1/?authMechanism=GSSAPI");
```

or using the legacy MongoClient API:

```java
MongoClientURI uri = new MongoClientURI("mongodb://username%40REALM.ME@host1/?authMechanism=GSSAPI");
```
{{%note%}}

The method refers to the `GSSAPI` authentication mechanism instead
of `Kerberos` because technically the driver authenticates via
the [`GSSAPI`](https://tools.ietf.org/html/rfc4752) SASL mechanism.

{{%/note%}}

To successfully authenticate via Kerberos, the application typically
must specify several system properties so that the underlying GSSAPI
Java libraries can acquire a Kerberos ticket:

```java
java.security.krb5.realm=MYREALM.ME
java.security.krb5.kdc=mykdc.myrealm.me
```

Depending on the Kerberos setup, additional property specifications may be required, either via the application code or, in some cases, the [withMechanismProperty()]({{<apiref "com/mongodb/MongoCredential.html#withMechanismProperty-java.lang.String-T-">}}) method of the `MongoCredential` instance:

- **[`SERVICE_NAME`]({{< apiref "com/mongodb/MongoCredential.html#SERVICE_NAME_KEY" >}})**


- **[`CANONICALIZE_HOST_NAME`]({{< apiref "com/mongodb/MongoCredential.html#CANONICALIZE_HOST_NAME_KEY" >}})**


- **[`JAVA_SUBJECT`]({{< apiref "com/mongodb/MongoCredential.html#JAVA_SUBJECT_KEY" >}})**

- **[`JAVA_SASL_CLIENT_PROPERTIES`]({{< apiref "com/mongodb/MongoCredential.html#JAVA_SASL_CLIENT_PROPERTIES_KEY" >}})**

For example, to specify the `SERVICE_NAME` property via the `MongoCredential` object:


```java
credential = credential.withMechanismProperty(MongoCredential.SERVICE_NAME_KEY, "othername");
```

Or via the `ConnectionString`:

```
mongodb://username%40MYREALM.com@myserver/?authMechanism=GSSAPI&authMechanismProperties=SERVICE_NAME:othername
```

{{% note %}}
On Windows, Oracle's JRE uses [LSA](https://msdn.microsoft.com/en-us/library/windows/desktop/aa378326.aspx) rather than 
[SSPI](https://msdn.microsoft.com/en-us/library/windows/desktop/aa380493.aspx) in its implementation of GSSAPI, which limits
interoperability with Windows Active Directory and in particular the ability to implement single sign-on. 

- [JDK-8054026](https://bugs.openjdk.java.net/browse/JDK-8054026) 
- [JDK-6722928](https://bugs.openjdk.java.net/browse/JDK-6722928) 
- [SO 23427343](https://stackoverflow.com/questions/23427343/cannot-retrieve-tgt-despite-allowtgtsessionkey-registry-entry)    
{{% /note %}}


## LDAP (PLAIN)

[MongoDB Enterprise](http://www.mongodb.com/products/mongodb-enterprise) supports proxy authentication through a Lightweight Directory Access Protocol (LDAP) service. To create a credential of type [LDAP]({{<apiref "core/authentication/#ldap-proxy-authority-authentication">}}) use the
[`createPlainCredential`]({{<apiref "com/mongodb/MongoCredential.html#createPlainCredential-java.lang.String-java.lang.String-char:A-">}}) static factory method:

```java
String user;          // The LDAP user name
char[] password;      // The LDAP password
// ...
MongoCredential credential = MongoCredential.createPlainCredential(user, "$external", password);
```

and then construct a MongoClient with that credential.  Using the new (since 3.7) MongoClient API:

```java
MongoClient mongoClient = MongoClients.create(
        MongoClientSettings.builder()
                .applyToClusterSettings(builder -> 
                        builder.hosts(Arrays.asList(new ServerAddress("host1", 27017))))
                .credential(credential)
                .build());
```

or using the legacy MongoClient API:

```java
MongoClient mongoClient = new MongoClient(new ServerAddress("host1", 27017), credential);
```

Or use a connection string that explicitly specifies the `authMechanism=PLAIN`.  Using the new MongoClient API:
                                                                                      
```java
MongoClient mongoClient = MongoClients.create("mongodb://user1@host1/?authSource=$external&authMechanism=PLAIN");
```

or using the legacy MongoClient API:

```java
MongoClientURI uri = new MongoClientURI("mongodb://user1@host1/?authSource=$external&authMechanism=PLAIN");
```

{{%note%}}
The method refers to the `plain` authentication mechanism instead
of `LDAP` because technically the driver authenticates via the
[`PLAIN`](https://www.ietf.org/rfc/rfc4616.txt) SASL mechanism.
{{%/note%}}
