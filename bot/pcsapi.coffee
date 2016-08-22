
module.exports = (robot) ->

  robot.hear /pcsapi list/i ,(msg) ->
    msg.send "/validate  	/subscriptions  		/tokens/ufmi 		/tokenMulti"
 

  robot.hear /pcsapi help/i ,(msg) ->
      msg.send({"attachments": [
        {
            "pretext": "토큰이 있는지 확인한다",
            "text": "*사용법*\n pcsapi validate {token_id}\n *사용예제*\n ```pcsapi validate 01d2378ef7b94af7982d779```\n",
            "mrkdwn_in": ["text", "pretext"]
        },
       {
            "pretext": "subscription 정보를 확인한다",
            "text": "*사용법*\n pcsapi subscriptions {token_id}\n *사용예제*\n ```pcsapi subscriptions 01d2378ef7b94af7982d779```\n",
            "mrkdwn_in": ["text", "pretext"]
        },
        {
            "pretext": "ufmi 정보를 확인한다",
            "text": "*사용법*\n pcsapi tokens/ufmi {ufmi_id}\n *사용예제*\n ```pcsapi tokens/ufmi 82*40*1234```\n",
            "mrkdwn_in": ["text", "pretext"]
        },
        {
            "pretext": "전화번호의 정보를 확인한다",
            "text": "*사용법*\n pcsapi tokensMulti {전화번호}\n *사용예제*\n ```pcsapi tokensMulti +821015460101```\n",
            "mrkdwn_in": ["text", "pretext"]
        }
    ]})
 
  robot.hear /pcsapi (.*) (.*)/i, (msg) ->
    url = escape(msg.match[1])
    console.log {url}
    pathPram=escape(msg.match[2])
    console.log {pathPram}
    msg.http("http://14.63.217.141:3000/v1/#{url}/#{pathPram}")
      .headers('X-ApiKey':'KTPJAAS', 'Content-Type': 'application/json')
      .get() (err, res, body) ->
        console.log {body}
        switch res.statusCode
          when 200
            msg.send "Good, #{body}"
          when 404
            msg.send "404 찾을 수 없습니다 "
          when 401
            msg.send "401 요청권한이 없습니다 "
          else
            msg.send "요청에 실패 하였습니다 "
