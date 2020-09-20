![Maven Central](https://img.shields.io/maven-central/v/site.kason/fhc.svg)

A http client library with fluent api

# Installation
    
### Gradle
    
    compile 'site.kason:fhc:2.0.0'
    
# Basic Usage

    val client = HttpClient.newClient();
    //request with GET method
    try(val resp = client.get("http://kason.site")) {
        assert resp.status() == 200;
        val data = resp.body()?.string();
        println(data);
    }
    
# POST form(x-www-form-urlencoded)

    val params = ["name": "fhc"];//key-value map
    try (val resp = client.post("http://kason.site", params)) {
        //process resp
    }
    
# POST json

    val json = '''{"name":"fhc"}''';//json string
    try (val resp = client.post("http://kason.site", json.getBytes("utf8")) {
        //process resp
    }
    
    
# Using proxy

    val client = HttpClient.config()
        .httpProxy("localhost", 8888)
        .httpsProxy("localhost", 8888)
        .newClient();
        
# Disable certification verification

    val client = HttpClient.config()
        .trustAll(true)
        .newClient();
        
    