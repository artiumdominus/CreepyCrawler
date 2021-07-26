# Creepy Crawler

Creepy Crawler is a service made with Spark Framework (https://sparkjava.com) that run web crawlers in search of specific keywords.

## Before run -> Configuration

Creepy Crawler is configured via these environment variables:

<table>
<tr>
  <td><b>BASE_URL</b> [required]</td>
  <td>Defines the first page to be visited by each web crawler;<br>Defines the scope of the analysis, where only adresses nested to this one can be accessed by the crawlers;</td>
</tr>
<tr>
  <td><b>MAX_RESULTS</b> [optional]</td>
  <td>Defines the maximum number of page addresses that can be accumulated in an analysis. When reached, the crawler must stop immediately</td>
</tr>
</table>

Attention: BASE_URL must be setted or the server will not start.

## Running

If you want to run locally, you need maven installed: https://maven.apache.org

Just run the command: `mvn clean verify exec:java`

or you can run over Docker:
`docker build . -t artiumdominus/creepycrawler`
`docker run -e BASE_URL=https://github.com -p 4567:4567 --rm artiumdominus/creepycrawler`

## API

As suggested in the docker command above, Spark init the server in the port 4567 by default.

#### POST /crawls
Inits a new search analysis, seeking for the informed keyword (the keyword must have between 4 and 32 characters). Then returns an 8-character unique id.

##### req
```json
{ "keyword": "cybernetics" }
```

##### res
```json
200 OK
{ "id": "#######" }
```

```json
400 BAD REQUEST
{ "status": 400, "message": "field 'keyword' is required (from 4 up to 32 chars)" }
```

#### GET /crawls/:id
Fetch the analysis data of a given search. The status can be either "active" or "done"

##### res
```json
200 OK
{
  "id": "########",
  "status": "active",
  "urls": [
    "https://github.com/",
    "https://github.com/artiumdominus",
    "https://github.com/artiumdominus/CreepyCrawler"
  ]
}
```

```json
404 NOT FOUND
{ "status": 404, "message": "crawl not found: ########" }
```

#### GET /crawls

Get the analysis ids list.

##### res
```json
200 OK
["########"]
```

#### DELETE /crawls/:id
Deletes a given search analysis, it's only possible to delete the searchs that already stopped.

```json
200 OK
"ok"
```

```json
400 BAD REQUEST
{ "status": 400, "message": "crawl ######## is still active." }
```

```json
404 NOT FOUND
{ "status": 404, "message": "crawl not found: ########" }
```
