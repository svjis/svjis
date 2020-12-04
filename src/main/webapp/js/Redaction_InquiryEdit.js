"use strict";

let noDelete = getLastOptionNo();
let desc = document.getElementById('opt_parent').getElementsByTagName('th')[0].innerHTML;


function getLastOptionNo() {
    let i = 0;
    let p = null;

    do {
        i++;
        p = document.getElementById('opt_' + i);
    } while (p !== null);

    return i - 1;
}

function addOption() {
    let lastOptionNo = getLastOptionNo();
    lastOptionNo++;
    let html =  '    <th scope="row" style="text-align: left">' + desc.replace('1', lastOptionNo) + '</th>\n' +
                '    <td>\n' +
                '        <input type="hidden" name="oid_' + lastOptionNo + '" value="0">\n' +
                '        <input class="common-input" id="o' + lastOptionNo + '-input" type="text" name="o_' + lastOptionNo + '" size="50" maxlength="250" value="">\n' +
                '    </td>\n' +
                '    <td>&nbsp;</td>\n';

    let p = document.getElementById('opt_parent');
    let newElement = document.createElement('tr');
    newElement.setAttribute('id', 'opt_' + lastOptionNo);
    newElement.innerHTML = html;
    p.appendChild(newElement);

    let button = document.getElementById('remove-option');
    button.disabled = false;
}

function removeOption() {
    let lastOptionNo = getLastOptionNo();

    if (lastOptionNo !== noDelete) {
        let element = document.getElementById('opt_' + lastOptionNo);
        element.parentNode.removeChild(element);
    }
    if (noDelete === (lastOptionNo - 1)) {
        let button = document.getElementById('remove-option');
        button.disabled = true;
    }
}
