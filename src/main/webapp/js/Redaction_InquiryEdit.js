"use strict";

var noDelete = getLastOptionNo();
var desc = document.getElementsByTagName('th')[0].innerHTML;


function getLastOptionNo() {
    var i = 0;
    var p = null;

    do {
        i++;
        p = document.getElementById('opt_' + i);
    } while (p !== null);

    return i - 1;
}

function addOption() {
    var lastOptionNo = getLastOptionNo();
    lastOptionNo++;
    var html =  '    <th scope="row" style="text-align: left">' + desc.replace(1, lastOptionNo) + '</th>\n' +
                '    <td>\n' +
                '        <input type="hidden" name="oid_' + lastOptionNo + '" value="0">\n' +
                '        <input id="common-input" type="text" name="o_' + lastOptionNo + '" size="50" maxlength="250" value="">\n' +
                '    </td>\n' +
                '    <td>&nbsp;</td>\n';

    var p = document.getElementById('opt_parent');
    var newElement = document.createElement('tr');
    newElement.setAttribute('id', 'opt_' + lastOptionNo);
    newElement.innerHTML = html;
    p.appendChild(newElement);

    var button = document.getElementById('remove-option');
    button.disabled = false;
}

function removeOption() {
    var lastOptionNo = getLastOptionNo();

    if (lastOptionNo !== noDelete) {
        var element = document.getElementById('opt_' + lastOptionNo);
        element.parentNode.removeChild(element);
    }
    if (noDelete === (lastOptionNo - 1)) {
        var button = document.getElementById('remove-option');
        button.disabled = true;
    }
}
