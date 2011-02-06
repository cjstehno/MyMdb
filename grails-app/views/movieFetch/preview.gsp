%{--
  - Copyright (c) 2011 Christopher J. Stehno (chris@stehno.com)
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  - http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  --}%
<div>
    <h1 style='margin-bottom:5px;border-bottom:1px dashed gray;padding:4px;'>${title} (${releaseYear})</h1>

    <div style='float:left'><img src='${poster}' style='margin:10px;border:1px solid gray;' width='160' /></div>

    <div style='float:right;width:400px;'>
        <div style='margin-bottom:8px;'><b>MPAA Rating:</b> ${mpaaRating}</div>
        <div style='margin-bottom:8px;'><b>Runtime:</b> ${runtime} min</div>

        <div><b>Genre:</b></div>
        <div style='margin-bottom:8px;margin-left:8px;'>
            <g:each in="${genres}" var="g">
                <span>${g}</span>,
            </g:each>
        </div>

        <div><b>Actors:</b></div>
        <div style='margin-bottom:8px;margin-left:8px;'>
            <g:each in="${actors}" var="a">
                <span>${a}</span>,
            </g:each>
        </div>

        <div><b>Sites:</b></div>
        <div style='margin-bottom:8px;margin-left:8px;'>
            <g:each in="${sites}" var="s">
                <div><b>${s.name}:</b> <a href="${s.url}" target="_blank">${s.url}</a></div>
            </g:each>
        </div>

        <div><b>Description:</b></div>
        <div style='margin-left:8px;'>${description}</div>
    </div>
</div>
