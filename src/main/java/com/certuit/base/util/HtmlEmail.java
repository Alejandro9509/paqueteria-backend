package com.certuit.base.util;

public class HtmlEmail {

    public static String htmlBody = """
            <!doctype html>
                <html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml" xmlns:o="urn:schemas-microsoft-com:office:office">
                  <head>
                    <title>
            
                    </title>
                    <!--[if !mso]><!-- -->
                    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                    <!--<![endif]-->
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <style type="text/css">
                      #outlook a { padding:0; }
                      body { margin:0;padding:0;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%; }
                      table, td { border-collapse:collapse;mso-table-lspace:0pt;mso-table-rspace:0pt; }
                      img { border:0;height:auto;line-height:100%; outline:none;text-decoration:none;-ms-interpolation-mode:bicubic; }
                      p { display:block;margin:13px 0; }
                    </style>
                    <!--[if mso]>
                    <xml>
                    <o:OfficeDocumentSettings>
                      <o:AllowPNG/>
                      <o:PixelsPerInch>96</o:PixelsPerInch>
                    </o:OfficeDocumentSettings>
                    </xml>
                    <![endif]-->
                    <!--[if lte mso 11]>
                    <style type="text/css">
            
            
                      .outlook-group-fix { width:100% !important; }
                    </style>
                    <![endif]-->
            
                  <!--[if !mso]><!-->
                    <link href="https://fonts.googleapis.com/css?family=Ubuntu:300,400,500,700" rel="stylesheet" type="text/css">
                    <link href="http://localhost:8091/css/main.08c24f53.scss" rel="stylesheet">
                    <style type="text/css">
                      @import url(https://fonts.googleapis.com/css?family=Ubuntu:300,400,500,700);
                    </style>
                  <!--<![endif]-->
            
            
                <style type="text/css">
                  @media only screen and (max-width:480px) {
                    .mj-column-per-66 { width:66.66666666666666% !important; max-width: 66.66666666666666%; }
            .mj-column-per-33 { width:33.33333333333333% !important; max-width: 33.33333333333333%; }
            .mj-column-per-25 { width:25% !important; max-width: 25%; }
            .mj-column-per-75 { width:75% !important; max-width: 75%; }
            .mj-column-per-100 { width:100% !important; max-width: 100%; }
            .mj-column-per-50 { width:50% !important; max-width: 50%; }
                  }
                </style>
            
            
                    <style type="text/css">
            
            
            
                @media only screen and (max-width:480px) {
                  table.full-width-mobile { width: 100% !important; }
                  td.full-width-mobile { width: auto !important; }
                }
            
                    </style>
                    <style type="text/css">.hide_on_mobile { display: none !important;}
                    @media only screen and (min-width: 480px) { .hide_on_mobile { display: block !important;} }
                    .hide_section_on_mobile { display: none !important;}
                    @media only screen and (min-width: 480px) {
                        .hide_section_on_mobile {
                            display: table !important;
                        }
            
                        div.hide_section_on_mobile {
                            display: block !important;
                        }
                    }
                    .hide_on_desktop { display: block !important;}
                    @media only screen and (min-width: 480px) { .hide_on_desktop { display: none !important;} }
                    .hide_section_on_desktop {
                        display: table !important;
                        width: 100%;
                    }
                    @media only screen and (min-width: 480px) { .hide_section_on_desktop { display: none !important;} }
            
                      p, h1, h2, h3 {
                          margin: 0px;
                      }
            
                      ul, li, ol {
                        font-size: 11px;
                        font-family: Ubuntu, Helvetica, Arial;
                      }
            
                      a {
                          text-decoration: none;
                          color: inherit;
                      }
            
                      @media only screen and (max-width:480px) {
            
                        .mj-column-per-100 { width:100%!important; max-width:100%!important; }
                        .mj-column-per-100 > .mj-column-per-75 { width:75%!important; max-width:75%!important; }
                        .mj-column-per-100 > .mj-column-per-60 { width:60%!important; max-width:60%!important; }
                        .mj-column-per-100 > .mj-column-per-50 { width:50%!important; max-width:50%!important; }
                        .mj-column-per-100 > .mj-column-per-40 { width:40%!important; max-width:40%!important; }
                        .mj-column-per-100 > .mj-column-per-33 { width:33.333333%!important; max-width:33.333333%!important; }
                        .mj-column-per-100 > .mj-column-per-25 { width:25%!important; max-width:25%!important; }
            
                        .mj-column-per-100 { width:100%!important; max-width:100%!important; }
                        .mj-column-per-75 { width:100%!important; max-width:100%!important; }
                        .mj-column-per-60 { width:100%!important; max-width:100%!important; }
                        .mj-column-per-50 { width:100%!important; max-width:100%!important; }
                        .mj-column-per-40 { width:100%!important; max-width:100%!important; }
                        .mj-column-per-33 { width:100%!important; max-width:100%!important; }
                        .mj-column-per-25 { width:100%!important; max-width:100%!important; }
                    }
                    @import "https://fonts.googleapis.com/css?family=Montserrat:400,700|Raleway:300,400";
                    /* colors */
                    /* tab setting */
                    /* breakpoints */
                    /* selectors relative to radio inputs */
                    html {
                        width: 100%;
                        height: 100%;
                    }
            
            
                    body {
                        background: #efefef;
                        color: #333;
                        font-family: "Raleway";
                        height: 100%;
                    }
                    body h1 {
                        text-align: center;
                        color: #F9A03E;
                        font-weight: 300;
                        padding: 40px 0 20px 0;
                        margin: 0;
                    }
            
                    .tabs {
                        left: 50% !important;
                        transform: translateX(-50%) !important;
                        position: relative !important;
                        background: white !important;
                        padding-bottom: 20px !important;
                        border-radius: 5px !important;
                        min-width: 240px !important;
                    }
                    .tabs input[name=tab-control] {
                        display: none !important;
                    }
                    .tabs .content section h2,
                    .tabs ul li label {
                        font-family: "Montserrat";
                        font-weight: bold;
                        font-size: 18px;
                        color: #F9A03E;
                    }
                    .tabs ul {
                        list-style-type: none !important;
                        padding-left: 0;
                        display: flex !important;
                        flex-direction: row !important;
                        margin-bottom: 10px;
                        justify-content: space-between !important;
                        align-items: flex-end !important;
                        flex-wrap: wrap !important;
                    }
                    .tabs ul li {
                        box-sizing: border-box !important;
                        flex: 1 !important;
                        width: 50% !important;
                        padding: 0 10px !important;
                        text-align: center !important;
                    }
                    .tabs ul li label {
                        transition: all 0.3s ease-in-out !important;
                        color: #929daf !important;
                        padding: 5px auto !important;
                        overflow: hidden !important;
                        text-overflow: ellipsis !important;
                        display: block !important;
                        cursor: pointer !important;
                        transition: all 0.2s ease-in-out !important;
                        white-space: nowrap !important;
                        -webkit-touch-callout: none !important;
                        -webkit-user-select: none !important;
                        -moz-user-select: none !important;
                        -ms-user-select: none !important;
                        user-select: none !important;
                    }
                    .tabs ul li label br {
                        display: none !important;
                    }
                    .tabs ul li label svg {
                        fill: #929daf !important;
                        height: 1.2em !important;
                        vertical-align: bottom !important;
                        margin-right: 0.2em !important;
                        transition: all 0.2s ease-in-out !important;
                    }
                    .tabs ul li label:hover, .tabs ul li label:focus, .tabs ul li label:active {
                        outline: 0 !important;
                        color: #bec5cf !important;
                    }
                    .tabs ul li label:hover svg, .tabs ul li label:focus svg, .tabs ul li label:active svg {
                        fill: #bec5cf !important;
                    }
                    .tabs .slider {
                        position: relative !important;
                        width: 50% !important;
                        transition: all 0.33s cubic-bezier(0.38, 0.8, 0.32, 1.07) !important;
                    }
                    .tabs .slider .indicator {
                        position: relative !important;
                        width: 50px !important;
                        max-width: 100% !important;
                        margin: 0 auto !important;
                        height: 4px !important;
                        background: #F9A03E !important;
                        border-radius: 1px !important;
                    }
                    .tabs .content {
                        margin-top: 30px !important;
                    }
                    .tabs .content section {
                        display: none !important;
                        -webkit-animation-name: content !important;
                        animation-name: content !important;
                        -webkit-animation-direction: normal !important;
                        animation-direction: normal !important;
                        -webkit-animation-duration: 0.3s !important;
                        animation-duration: 0.3s !important;
                        -webkit-animation-timing-function: ease-in-out !important;
                        animation-timing-function: ease-in-out !important;
                        -webkit-animation-iteration-count: 1 !important;
                        animation-iteration-count: 1 !important;
                        line-height: 1.4 !important;
                    }
                    .tabs .content section h2 {
                        color: #F9A03E !important;
                        display: none !important;
                    }
                    .tabs .content section h2::after {
                        content: "" !important;
                        position: relative !important;
                        display: block !important;
                        width: 30px !important;
                        height: 3px !important;
                        background: #F9A03E !important;
                        margin-top: 5px !important;
                        left: 1px !important;
                    }
                    .tabs input[name=tab-control]:nth-of-type(1):checked ~ ul > li:nth-child(1) > label {
                        cursor: default!important;
                        color: #F9A03E !important;
                    }
                    .tabs input[name=tab-control]:nth-of-type(1):checked ~ ul > li:nth-child(1) > label svg {
                        fill: #F9A03E !important;
                    }
                    @media (max-width: 600px) {
                        .tabs input[name=tab-control]:nth-of-type(1):checked ~ ul > li:nth-child(1) > label {
                            background: rgba(0, 0, 0, 0.08) !important;
                        }
                    }
                    .tabs input[name=tab-control]:nth-of-type(1):checked ~ .slider {
                        transform: translateX(0%) !important;
                    }
                    .tabs input[name=tab-control]:nth-of-type(1):checked ~ .content > section:nth-child(1) {
                        display: block !important;
                    }
                    .tabs input[name=tab-control]:nth-of-type(2):checked ~ ul > li:nth-child(2) > label {
                        cursor: default !important;
                        color: #F9A03E !important;
                    }
                    .tabs input[name=tab-control]:nth-of-type(2):checked ~ ul > li:nth-child(2) > label svg {
                        fill: #F9A03E !important;
                    }
                    @media (max-width: 600px) {
                        .tabs input[name=tab-control]:nth-of-type(2):checked ~ ul > li:nth-child(2) > label {
                            background: rgba(0, 0, 0, 0.08) !important;
                        }
                    }
                    .tabs input[name=tab-control]:nth-of-type(2):checked ~ .slider {
                        transform: translateX(100%) !important;
                    }
                    .tabs input[name=tab-control]:nth-of-type(2):checked ~ .content > section:nth-child(2) {
                        display: block !important;
                    }
                    .tabs input[name=tab-control]:nth-of-type(3):checked ~ ul > li:nth-child(3) > label {
                        cursor: default !important;
                        color: #F9A03E !important;
                    }
                    .tabs input[name=tab-control]:nth-of-type(3):checked ~ ul > li:nth-child(3) > label svg {
                        fill: #F9A03E !important;
                    }
                    @media (max-width: 600px) {
                        .tabs input[name=tab-control]:nth-of-type(3):checked ~ ul > li:nth-child(3) > label {
                            background: rgba(0, 0, 0, 0.08) !important;
                        }
                    }
                    .tabs input[name=tab-control]:nth-of-type(3):checked ~ .slider {
                        transform: translateX(200%) !important;
                    }
                    .tabs input[name=tab-control]:nth-of-type(3):checked ~ .content > section:nth-child(3) {
                        display: block !important;
                    }
                    .tabs input[name=tab-control]:nth-of-type(4):checked ~ ul > li:nth-child(4) > label {
                        cursor: default !important;
                        color: #F9A03E !important;
                    }
                    .tabs input[name=tab-control]:nth-of-type(4):checked ~ ul > li:nth-child(4) > label svg {
                        fill: #F9A03E !important;
                    }
                    @media (max-width: 600px) {
                        .tabs input[name=tab-control]:nth-of-type(4):checked ~ ul > li:nth-child(4) > label {
                            background: rgba(0, 0, 0, 0.08) !important;
                        }
                    }
                    .tabs input[name=tab-control]:nth-of-type(4):checked ~ .slider {
                        transform: translateX(300%) !important;
                    }
                    .tabs input[name=tab-control]:nth-of-type(4):checked ~ .content > section:nth-child(4) {
                        display: block !important;
                    }
                    @-webkit-keyframes content {
                        from {
                            opacity: 0 !important;
                            transform: translateY(5%) !important;
                        }
                        to {
                            opacity: 1 !important;
                            transform: translateY(0%) !important;
                        }
                    }
                    @keyframes content {
                        from {
                            opacity: 0 !important;
                            transform: translateY(5%) !important;
                        }
                        to {
                            opacity: 1 !important;
                            transform: translateY(0%) !important;
                        }
                    }
                    @media (max-width: 1000px) {
                        .tabs ul li label {
                            white-space: initial !important;
                        }
                        .tabs ul li label br {
                            display: initial !important;
                        }
                        .tabs ul li label svg {
                            height: 1.5em !important;
                        }
                    }
                    @media (max-width: 600px) {
                        .tabs ul li label {
                            padding: 5px !important;
                            border-radius: 5px !important;
                        }
                        .tabs ul li label span {
                            display: none !important;
                        }
                        .tabs .slider {
                            display: none !important;
                        }
                        .tabs .content {
                            margin-top: 20px !important;
                        }
                        .tabs .content section h2 {
                            display: block !important;
                        }
                    }
            
                    .history-tl-container{
                        font-family: "Roboto",sans-serif !important;
                        width:50% !important;
                        margin:auto !important;
                        display:block !important;
                        position:relative !important;
                    }
                    .history-tl-container ul.tl{
                        margin:20px 0 !important;
                        padding:0 !important;
                        display:inline-block !important;
            
                    }
                    .history-tl-container ul.tl li{
                        list-style: none !important;
                        margin:auto !important;
                        margin-left:200px !important;
                        min-height:50px !important;
                        /*background: rgba(255,255,0,0.1);*/
                        border-left:1px dashed #86D6FF !important;
                        padding:0 0 50px 30px !important;
                        position:relative !important;
                    }
                    .history-tl-container ul.tl li:last-child{ border-left:0;}
                    .history-tl-container ul.tl li::before{
                        position: absolute !important;
                        left: -18px !important;
                        top: -5px !important;
                        content: " " !important;
                        border: 8px solid rgba(255, 255, 255, 0.74) !important;
                        border-radius: 500% !important;
                        background: #F9A03E !important;
                        height: 20px !important;
                        width: 20px !important;
                        transition: all 500ms ease-in-out !important;
            
                    }
                    .history-tl-container ul.tl li:hover::before{
                        border-color:  #F9A03E !important;
                        transition: all 1000ms ease-in-out !important;
                    }
                    ul.tl li .item-title{
                    }
                    ul.tl li .item-detail{
                        color:rgba(0,0,0,0.5) !important;
                        font-size:12px !important;
                    }
                    ul.tl li .timestamp{
                        color: #8D8D8D !important;
                        position: absolute !important;
                        width:100px !important;
                        left: -75% !important;
                        text-align: right !important;
                        font-size: 12px !important;
                    }
            
                    .vcv-timeline {
                        position: relative !important;
                        display: flex !important;
                        align-items: center !important;
                        max-width: 930px !important;
                        margin: 0 auto !important;
                        padding: 0 !important;
                        list-style-type: none !important;
                        color: #a7a7a7 !important;
                    }
                    .vcv-timeline-item {
                        display: flex !important;
                        align-items: center !important;
                        flex: 1 0 auto !important;
                        padding: 0 !important;
                    }
                    .vcv-timeline-item::before,
                    .vcv-timeline-item::after {
                        content: '' !important;
                    }
                    .vcv-timeline-item::before {
                        content: attr(data-step) !important;
                        display: inline-flex !important;
                        justify-content: center !important;
                        align-items: center !important;
                        flex: 0 0 32px !important;
                        height: 32px !important;
                        margin: 0 10px 0 0 !important;
                        border-radius: 50% !important;
                        border: 2px solid #a7a7a7 !important;
                        color: #a7a7a7 !important;
                    }
                    .vcv-timeline-item::after {
                        height: 2px !important;
                        background: #a7a7a7 !important;
                        width: 100% !important;
                        margin: 0 10px !important;
                    }
                    .vcv-timeline-item:last-of-type {
                        flex: 0 0 120px !important;
                    }
                    .vcv-timeline-item:last-of-type::after {
                        display: none !important;
                    }
                    .vcv-timeline-item.vcv-step-done::before {
                        content: '' !important;
                        border-color: #aace35 !important;
                        background: #aace35 url("data:image/svg+xml,%0A%3Csvg width='14px' height='11px' viewBox='0 0 14 11' version='1.1' xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink'%3E%3Cg id='Page-1' stroke='none' stroke-width='1' fill='none' fill-rule='evenodd'%3E%3Cg id='Activation-Theme-to-Premium' transform='translate(-827.000000, -183.000000)' fill='%23FFFFFF' fill-rule='nonzero'%3E%3Cg id='Activation-02' transform='translate(342.000000, 172.000000)'%3E%3Cg id='Navigation' transform='translate(44.000000, 0.000000)'%3E%3Cg id='check' transform='translate(441.000000, 11.000000)'%3E%3Cpolygon id='Path' points='11.8606017 0 4.86058121 6.82615443 2.13952103 4.17292819 0 6.25973131 4.8606221 11 14 2.08684301'%3E%3C/polygon%3E%3C/g%3E%3C/g%3E%3C/g%3E%3C/g%3E%3C/g%3E%3C/svg%3E") no-repeat center !important;
                        background-size: 40% !important;
                    }
                    .vcv-timeline-item.vcv-step-done::after {
                        background: #aace35 !important;
                    }
                    @media screen and (max-width: 768px) {
                        .vcv-timeline {
                            margin-bottom: 50px !important;
                        }
                        .vcv-timeline-item {
                            position: relative !important;
                        }
                        .vcv-timeline-item span {
                            text-align: left !important;
                            margin: 10px 0 0 !important;
                            position: absolute !important;
                            top: 30px !important;
                            width: 100% !important;
                            left: 2px !important;
                        }
                        .vcv-timeline-item:last-of-type {
                            flex: 0 0 auto !important;
                            margin: 0 30px 0 0 !important;
                        }
                        .vcv-timeline-item::before {
                            margin: 0 !important;
                        }
                        .vcv-timeline-item::after {
                            flex: 0 1 auto !important;
                        }
                    }
                    </style>
            
                  </head>
                  <body style="background-color:#f0f0f0;">
            
            
                  <div style="background-color:#f0f0f0;">
            
                  <table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="background:#f0f0f0;background-color:#f0f0f0;width:100%;">
                    <tbody>
                      <tr>
                        <td>
            
            
                  <!--[if mso | IE]>
                  <table
                     align="center" border="0" cellpadding="0" cellspacing="0" class="" style="width:600px;" width="600"
                  >
                    <tr>
                      <td style="line-height:0px;font-size:0px;mso-line-height-rule:exactly;">
                  <![endif]-->
            
            
                  <div style="margin:0px auto;max-width:80%;">
            
                    <table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="width:100%;">
                      <tbody>
                        <tr>
                          <td style="direction:ltr;font-size:0px;padding:5px 0px 5px 0px;text-align:center;">
                            <!--[if mso | IE]>
                              <table role="presentation" border="0" cellpadding="0" cellspacing="0">
            
                    <tr>
            
                        <td
                           class="" style="vertical-align:middle;width:399.99999999999994px;"
                        >
                      <![endif]-->
            
                  <div class="mj-column-per-66 outlook-group-fix" style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:middle;width:66.66666666666666%;">
            
                  <table border="0" cellpadding="0" cellspacing="0" role="presentation" style="vertical-align:middle;" width="100%">
            
                        <tr>
                          <td style="font-size:0px;word-break:break-word;">
            
            
                <!--[if mso | IE]>
            
                    <table role="presentation" border="0" cellpadding="0" cellspacing="0"><tr><td height="10" style="vertical-align:top;height:10px;">
            
                <![endif]-->
            
                  <div style="height:10px;">
                    &nbsp;
                  </div>
            
                <!--[if mso | IE]>
            
                    </td></tr></table>
            
                <![endif]-->
            
            
                          </td>
                        </tr>
            
                  </table>
            
                  </div>
            
                      <!--[if mso | IE]>
                        </td>
            
                        <td
                           class="" style="vertical-align:middle;width:199.99999999999997px;"
                        >
                      <![endif]-->
            
                  <div class="mj-column-per-33 outlook-group-fix" style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:middle;width:33.33333333333333%;">
            
                  <table border="0" cellpadding="0" cellspacing="0" role="presentation" style="vertical-align:middle;" width="100%">
            
                        <tr>
                          <td style="font-size:0px;word-break:break-word;">
            
            
                <!--[if mso | IE]>
            
                    <table role="presentation" border="0" cellpadding="0" cellspacing="0"><tr><td height="10" style="vertical-align:top;height:10px;">
            
                <![endif]-->
            
                  <div style="height:10px;">
                    &nbsp;
                  </div>
            
                <!--[if mso | IE]>
            
                    </td></tr></table>
            
                <![endif]-->
            
            
                          </td>
                        </tr>
            
                  </table>
            
                  </div>
            
                      <!--[if mso | IE]>
                        </td>
            
                    </tr>
            
                              </table>
                            <![endif]-->
                          </td>
                        </tr>
                      </tbody>
                    </table>
            
                  </div>
            
            
                  <!--[if mso | IE]>
                      </td>
                    </tr>
                  </table>
                  <![endif]-->
            
            
                        </td>
                      </tr>
                    </tbody>
                  </table>
            
            
                  <!--[if mso | IE]>
                  <table
                     align="center" border="0" cellpadding="0" cellspacing="0" class="" style="width:600px;" width="600"
                  >
                    <tr>
                      <td style="line-height:0px;font-size:0px;mso-line-height-rule:exactly;">
                  <![endif]-->
            
            
                  <div style="background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:80%;">
            
                    <table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="background:#ffffff;background-color:#ffffff;width:100%;">
                      <tbody>
                        <tr>
                          <td style="direction:ltr;font-size:0px;padding:9px 0px 0px 0px;text-align:center;">
                            <!--[if mso | IE]>
                              <table role="presentation" border="0" cellpadding="0" cellspacing="0">
            
                    <tr>
            
                        <td
                           class="" style="vertical-align:top;width:150px;"
                        >
                      <![endif]-->
            
                  <div class="mj-column-per-25 outlook-group-fix" style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:25%;">
            
                  <table border="0" cellpadding="0" cellspacing="0" role="presentation" style="vertical-align:top;" width="100%">
            
                        <tr>
                          <td align="right" style="font-size:0px;padding:0px 0px 0px 0px;word-break:break-word;">
            
                  <table border="0" cellpadding="0" cellspacing="0" role="presentation" style="border-collapse:collapse;border-spacing:0px;">
                    <tbody>
                      <tr>
                        <td style="width:60px;">
            
                  <img height="auto" src="https://s3-eu-west-1.amazonaws.com/topolio/uploads/61846b9f99fca/1636070646.jpg" style="border:0;display:block;outline:none;text-decoration:none;height:auto;width:100%;font-size:13px;" width="60">
            
                        </td>
                      </tr>
                    </tbody>
                  </table>
            
                          </td>
                        </tr>
            
                  </table>
            
                  </div>
            
                      <!--[if mso | IE]>
                        </td>
            
                        <td
                           class="" style="vertical-align:top;width:450px;"
                        >
                      <![endif]-->
            
                  <div class="mj-column-per-75 outlook-group-fix" style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:75%;">
            
                  <table border="0" cellpadding="0" cellspacing="0" role="presentation" style="vertical-align:top;" width="100%">
            
                        <tr>
                          <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                  <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;"><p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;"><span style="font-size: 20px;">Hola, #CLIENTE</span></p></div>
            
                          </td>
                        </tr>
            
                  </table>
            
                  </div>
            
                      <!--[if mso | IE]>
                        </td>
            
                    </tr>
            
                              </table>
                            <![endif]-->
                          </td>
                        </tr>
                      </tbody>
                    </table>
            
                  </div>
            
            
                  <!--[if mso | IE]>
                      </td>
            
                    </tr>
                  </table>
            
                  <table
                     align="center" border="0" cellpadding="0" cellspacing="0" class="" style="width:600px;" width="600"
                  >
                    <tr>
                      <td style="line-height:0px;font-size:0px;mso-line-height-rule:exactly;">
                  <![endif]-->
            
            
                  <div style="background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:80%;">
            
                    <table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="background:#ffffff;background-color:#ffffff;width:100%;">
                      <tbody>
                        <tr>
                          <td style="direction:ltr;font-size:0px;padding:0px 0px 0px 0px;text-align:center;">
                            <!--[if mso | IE]>
                              <table role="presentation" border="0" cellpadding="0" cellspacing="0">
            
                    <tr>
            
                        <td
                           class="" style="vertical-align:middle;width:600px;"
                        >
                      <![endif]-->
            
                  <div class="mj-column-per-100 outlook-group-fix" style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:middle;width:100%;">
            
                  <table border="0" cellpadding="0" cellspacing="0" role="presentation" style="vertical-align:middle;" width="100%">
                      <tr>
                          <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                              <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;"><p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: center;"><span style="font-size: 18px;"> #MENSAJE</span></p></div>
            
                          </td>
                      </tr>
                        <tr>
                          <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                  <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;"><p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: center;"><span style="font-size: 18px;"><span style="color: #f9a03e;">N&uacute;mero de rastreo:</span> #TRACKING</span></p></div>
            
                          </td>
                        </tr>
            
                        <tr>
                          <td align="center" vertical-align="middle" style="font-size:0px;padding:0px 1px 0px 0px;padding-top:10;padding-bottom:45;word-break:break-word;">
            
                  <table border="0" cellpadding="0" cellspacing="0" role="presentation" style="border-collapse:separate;line-height:100%;">
                    <tr>
                      <td align="center" bgcolor="#F9A03E" role="presentation" style="border:none;border-radius:8px;cursor:auto;mso-padding-alt:8px 26px 9px 26px;background:#F9A03E;" valign="middle">
                        <a href="#LINK_TRACKING" style="display: inline-block; background: #F9A03E; color: #fffff; font-family: Ubuntu, Helvetica, Arial, sans-serif, Helvetica, Arial, sans-serif; font-size: 20px; font-weight: normal; line-height: 25px; margin: 0; text-decoration: none; text-transform: none; padding: 8px 26px 9px 26px; mso-padding-alt: 0px; border-radius: 8px;" target="_blank">
                          <span style="font-size: 20px;">Rastrear Paquete</span>
                        </a>
                      </td>
                    </tr>
                  </table>
            
                          </td>
                        </tr>
            
                  </table>
            
                  </div>
            
                      <!--[if mso | IE]>
                        </td>
            
                    </tr>
            
                              </table>
                            <![endif]-->
                          </td>
                        </tr>
                      </tbody>
                    </table>
            
                  </div>
            
            
                  <!--[if mso | IE]>
                      </td>
                    </tr>
                  </table>
            
                  <table
                     align="center" border="0" cellpadding="0" cellspacing="0" class="" style="width:600px;" width="600"
                  >
                    <tr>
                      <td style="line-height:0px;font-size:0px;mso-line-height-rule:exactly;">
                  <![endif]-->
            
            
                  <div style="background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:80%;">
            
                    <table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="background:#ffffff;background-color:#ffffff;width:100%;">
                      <tbody>
                        <tr>
                          <td style="direction:ltr;font-size:0px;padding:2px 0px 2px 0px;text-align:center;">
                            <!--[if mso | IE]>
                              <table role="presentation" border="0" cellpadding="0" cellspacing="0">
            
                    <tr>
            
                        <td
                           class="" style="vertical-align:top;width:600px;"
                        >
                      <![endif]-->
            
                  <div class="mj-column-per-100 outlook-group-fix" style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%;">
            
                  <table border="0" cellpadding="0" cellspacing="0" role="presentation" style="vertical-align:top;" width="100%">
            
                        <tr>
                          <td style="font-size:0px;padding:0px 5px;padding-top:5px;word-break:break-word;">
            
                  <p style="font-family: Ubuntu, Helvetica, Arial; border-top: solid 2px #F9A03E; font-size: 1; margin: 0px auto; width: 100%;">
                  </p>
            
                  <!--[if mso | IE]>
                    <table
                       align="center" border="0" cellpadding="0" cellspacing="0" style="border-top:solid 2px #F9A03E;font-size:1;margin:0px auto;width:580px;" role="presentation" width="580px"
                    >
                      <tr>
                        <td style="height:0;line-height:0;">
                          &nbsp;
                        </td>
                      </tr>
                    </table>
                  <![endif]-->
            
            
                          </td>
                        </tr>
            
                  </table>
            
                  </div>
            
                      <!--[if mso | IE]>
                        </td>
            
                    </tr>
            
                              </table>
                            <![endif]-->
                          </td>
                        </tr>
                      </tbody>
                    </table>
            
                  </div>
            
            
                  <!--[if mso | IE]>
                      </td>
                    </tr>
                  </table>
            
                  <table
                     align="center" border="0" cellpadding="0" cellspacing="0" class="" style="width:600px;" width="600"
                  >
                    <tr>
                      <td style="line-height:0px;font-size:0px;mso-line-height-rule:exactly;">
                  <![endif]-->
            
            
                  <div style="background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:80%;">
            
                    <table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="background:#ffffff;background-color:#ffffff;width:100%;">
                      <tbody>
                        <tr>
                          <td style="direction:ltr;font-size:0px;padding:0px 0px 0px 0px;text-align:center;">
                            <!--[if mso | IE]>
                              <table role="presentation" border="0" cellpadding="0" cellspacing="0">
            
                    <tr>
            
                        <td
                           class="" style="vertical-align:top;width:300px;"
                        >
                      <![endif]-->
            
                  <div class="mj-column-per-50 outlook-group-fix" style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:50%;">
            
                  <table border="0" cellpadding="0" cellspacing="0" role="presentation" style="vertical-align:top;" width="100%">
            
                        <tr>
                          <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                  <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;"><p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: center;"><span style="font-size: 18px;">
                      <span style="color: #f9a03e;">Origen:</span> #ORIGEN </span> </p></div>
            
                          </td>
                        </tr>
            
            
                  </table>
            
                  </div>
            
                      <!--[if mso | IE]>
                        </td>
            
                        <td
                           class="" style="vertical-align:top;width:300px;"
                        >
                      <![endif]-->
            
                  <div class="mj-column-per-50 outlook-group-fix" style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:50%;">
            
                  <table border="0" cellpadding="0" cellspacing="0" role="presentation" style="vertical-align:top;" width="100%">
            
                        <tr>
                          <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                  <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                      <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: center;">
                          <span style="font-size: 18px;">
                              <span style="color: #f9a03e;">Destino:</span> #DESTINO</span> </p></div>
            
                          </td>
                        </tr>
            
            
                  </table>
            
                  </div>
            
                      <!--[if mso | IE]>
                        </td>
            
                    </tr>
            
                              </table>
                            <![endif]-->
                          </td>
                        </tr>
                      </tbody>
                    </table>
            
                  </div>
            
            
                  <!--[if mso | IE]>
                      </td>
                    </tr>
                  </table>
            
                  <table
                     align="center" border="0" cellpadding="0" cellspacing="0" class="" style="width:600px;" width="600"
                  >
                    <tr>
                      <td style="line-height:0px;font-size:0px;mso-line-height-rule:exactly;">
                  <![endif]-->
            
            
                  <div style="background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:80%;">
            
                    <table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="background:#ffffff;background-color:#ffffff;width:100%;">
                      <tbody>
                        <tr>
                          <td style="direction:ltr;font-size:0px;padding:2px 0px 2px 0px;text-align:center;">
                            <!--[if mso | IE]>
                              <table role="presentation" border="0" cellpadding="0" cellspacing="0">
            
                    <tr>
            
                        <td
                           class="" style="vertical-align:top;width:600px;"
                        >
                      <![endif]-->
            
                  <div class="mj-column-per-100 outlook-group-fix" style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%;">
            
                  <table border="0" cellpadding="0" cellspacing="0" role="presentation" style="vertical-align:top;" width="100%">
            
                        <tr>
                          <td style="font-size:0px;padding:0px 5px;padding-top:5px;word-break:break-word;">
            
                  <p style="font-family: Ubuntu, Helvetica, Arial; border-top: solid 2px #F9A03E; font-size: 1; margin: 0px auto; width: 100%;">
                  </p>
            
                  <!--[if mso | IE]>
                    <table
                       align="center" border="0" cellpadding="0" cellspacing="0" style="border-top:solid 2px #F9A03E;font-size:1;margin:0px auto;width:580px;" role="presentation" width="580px"
                    >
                      <tr>
                        <td style="height:0;line-height:0;">
                          &nbsp;
                        </td>
                      </tr>
                    </table>
                  <![endif]-->
            
            
                          </td>
                        </tr>
            
                  </table>
            
                  </div>
            
                      <!--[if mso | IE]>
                        </td>
            
                    </tr>
            
                              </table>
                            <![endif]-->
                          </td>
                        </tr>
                      </tbody>
                    </table>
            
                  </div>
            
            
                  <!--[if mso | IE]>
                      </td>
                    </tr>
                  </table>
            
                  <table
                     align="center" border="0" cellpadding="0" cellspacing="0" class="" style="width:600px;" width="600"
                  >
                    <tr>
                      <td style="line-height:0px;font-size:0px;mso-line-height-rule:exactly;">
                  <![endif]-->
            
            
                  <div style="background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:80%;">
            
                    <table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="background:#ffffff;background-color:#ffffff;width:100%;">
                      <tbody>
                        <tr>
                          <td style="direction:ltr;font-size:0px;padding:0px 0px 0px 0px;text-align:center;">
                            <!--[if mso | IE]>
                              <table role="presentation" border="0" cellpadding="0" cellspacing="0">
            
                    <tr>
            
                        <td
                           class="" style="vertical-align:top;width:600px;"
                        >
                      <![endif]-->
            
                  <div class="mj-column-per-100 outlook-group-fix" style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%;">
            
                  <table border="0" cellpadding="0" cellspacing="0" role="presentation" style="vertical-align:top;" width="100%">
            
                        <tr>
                          <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                  <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;"><p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px;">
            
            
            <!--          PAQUETES-->
                                  <!-- inicio de paquetes-->
                      <!-- inicio de paquetes-->
                      #PAQUETES
                      <!-- fin de paquetes -->
                                  <!-- fin de paquetes -->
                  </div>
            
            
                          </td>
                        </tr>
            
                  </table>
            
                  </div>
            
                      <!--[if mso | IE]>
                        </td>
            
                    </tr>
            
                              </table>
                            <![endif]-->
                          </td>
                        </tr>
                      </tbody>
                    </table>
            
                  </div>
            
            
                  <!--[if mso | IE]>
                      </td>
                    </tr>
                  </table>
            
            
            
                  <table
                     align="center" border="0" cellpadding="0" cellspacing="0" class="" style="width:600px;" width="600"
                  >
                    <tr>
                      <td style="line-height:0px;font-size:0px;mso-line-height-rule:exactly;">
                  <![endif]-->
            
            
            
                  <!--[if mso | IE]>
                      </td>
                    </tr>
                  </table>
            
                  <table
                     align="center" border="0" cellpadding="0" cellspacing="0" class="" style="width:600px;" width="600"
                  >
                    <tr>
                      <td style="line-height:0px;font-size:0px;mso-line-height-rule:exactly;">
                  <![endif]-->
            
            
            
                  <!--[if mso | IE]>
                      </td>
                    </tr>
                  </table>
            
                  <table
                     align="center" border="0" cellpadding="0" cellspacing="0" class="" style="width:600px;" width="600"
                  >
                    <tr>
                      <td style="line-height:0px;font-size:0px;mso-line-height-rule:exactly;">
                  <![endif]-->
            
            
                  <div style="background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:80%;">
            
                    <table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="background:#ffffff;background-color:#ffffff;width:100%;">
                      <tbody>
                        <tr>
                          <td style="direction:ltr;font-size:0px;padding:0px 0px 0px 0px;text-align:center;">
                            <!--[if mso | IE]>
                              <table role="presentation" border="0" cellpadding="0" cellspacing="0">
            
                    <tr>
            
                        <td
                           class="" style="vertical-align:top;width:600px;"
                        >
                      <![endif]-->
            
                  <div class="mj-column-per-100 outlook-group-fix" style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%;">
            
                  <table border="0" cellpadding="0" cellspacing="0" role="presentation" width="100%">
                    <tbody>
                      <tr>
                        <td style="vertical-align:top;padding:0px 0px 10px 0px;">
            
                  <table border="0" cellpadding="0" cellspacing="0" role="presentation" style width="100%">
            
                        <tr>
                          <td style="font-size:0px;word-break:break-word;">
            
            
                <!--[if mso | IE]>
            
                    <table role="presentation" border="0" cellpadding="0" cellspacing="0"><tr><td height="20" style="vertical-align:top;height:20px;">
            
                <![endif]-->
            
                  <div style="height:20px;">
                    &nbsp;
                  </div>
            
                <!--[if mso | IE]>
            
                    </td></tr></table>
            
                <![endif]-->
            
            
                          </td>
                        </tr>
            
                  </table>
            
                        </td>
                      </tr>
                    </tbody>
                  </table>
            
                  </div>
            
                      <!--[if mso | IE]>
                        </td>
            
                    </tr>
            
                              </table>
                            <![endif]-->
                          </td>
                        </tr>
                      </tbody>
                    </table>
            
                  </div>
            
            
                  <!--[if mso | IE]>
                      </td>
                    </tr>
                  </table>
                  <![endif]-->
            
            
                  <table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="background:#f0f0f0;background-color:#f0f0f0;width:100%;">
                    <tbody>
                      <tr>
                        <td>
            
            
                  <!--[if mso | IE]>
                  <table
                     align="center" border="0" cellpadding="0" cellspacing="0" class="" style="width:600px;" width="600"
                  >
                    <tr>
                      <td style="line-height:0px;font-size:0px;mso-line-height-rule:exactly;">
                  <![endif]-->
            
            
                  <div style="margin:0px auto;max-width:80%;">
            
                    <table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation" style="width:100%;">
                      <tbody>
                        <tr>
                          <td style="direction:ltr;font-size:0px;padding:5px 0px 5px 0px;text-align:center;">
                            <!--[if mso | IE]>
                              <table role="presentation" border="0" cellpadding="0" cellspacing="0">
            
                    <tr>
            
                        <td
                           class="" style="vertical-align:middle;width:399.99999999999994px;"
                        >
                      <![endif]-->
            
                  <div class="mj-column-per-66 outlook-group-fix" style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:middle;width:66.66666666666666%;">
            
                  <table border="0" cellpadding="0" cellspacing="0" role="presentation" style="vertical-align:middle;" width="100%">
            
                        <tr>
                          <td style="font-size:0px;word-break:break-word;">
            
            
                <!--[if mso | IE]>
            
                    <table role="presentation" border="0" cellpadding="0" cellspacing="0"><tr><td height="10" style="vertical-align:top;height:10px;">
            
                <![endif]-->
            
                  <div style="height:10px;">
                    &nbsp;
                  </div>
            
                <!--[if mso | IE]>
            
                    </td></tr></table>
            
                <![endif]-->
            
            
                          </td>
                        </tr>
            
                  </table>
            
                  </div>
            
                      <!--[if mso | IE]>
                        </td>
            
                        <td
                           class="" style="vertical-align:middle;width:199.99999999999997px;"
                        >
                      <![endif]-->
            
                  <div class="mj-column-per-33 outlook-group-fix" style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:middle;width:33.33333333333333%;">
            
                  <table border="0" cellpadding="0" cellspacing="0" role="presentation" style="vertical-align:middle;" width="100%">
            
                        <tr>
                          <td style="font-size:0px;word-break:break-word;">
            
            
                <!--[if mso | IE]>
            
                    <table role="presentation" border="0" cellpadding="0" cellspacing="0"><tr><td height="10" style="vertical-align:top;height:10px;">
            
                <![endif]-->
            
                  <div style="height:10px;">
                    &nbsp;
                  </div>
            
                <!--[if mso | IE]>
            
                    </td></tr></table>
            
                <![endif]-->
            
            
                          </td>
                        </tr>
            
                  </table>
            
                  </div>
            
                      <!--[if mso | IE]>
                        </td>
            
                    </tr>
            
                              </table>
                            <![endif]-->
                          </td>
                        </tr>
                      </tbody>
                    </table>
            
                  </div>
            
            
                  <!--[if mso | IE]>
                      </td>
                    </tr>
                  </table>
                  <![endif]-->
            
            
                        </td>
                      </tr>
                    </tbody>
                  </table>
            
                  </div>
            
                  </body>
                </html>""";


    public static String htmlPaquetes = """
            <div>
            
                          <div style="background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:600px;">
            
                              <table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation"
                                     style="background:#ffffff;background-color:#ffffff;width:100%;">
                                  <tbody>
                                  <tr>
                                      <td style="direction:ltr;font-size:0px;padding:1px 0px 1px 0px;text-align:center;">
                                          <!--[if mso | IE]>
                                          <table role="presentation" border="0" cellpadding="0" cellspacing="0">
            
                                              <tr>
            
                                                  <td
                                                          class="" style="vertical-align:top;width:600px;"
                                                  >
                                          <![endif]-->
            
                                          <div class="mj-column-per-100 outlook-group-fix"
                                               style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%;">
            
                                              <table border="0" cellpadding="0" cellspacing="0" role="presentation"
                                                     style="vertical-align:top;" width="100%">
            
                                                  <tr>
                                                      <td align="left"
                                                          style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                                                          <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                              <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px;"><strong><span
                                                                      style="font-size: 16px;">Paquete #NUM_PAQUETE</span></strong></p></div>
            
                                                      </td>
                                                  </tr>
            
                                              </table>
            
                                          </div>
            
                                          <!--[if mso | IE]>
                                          </td>
            
                                          </tr>
            
                                          </table>
                                          <![endif]-->
                                      </td>
                                  </tr>
                                  </tbody>
                              </table>
            
                          </div>
            
            
                          <!--[if mso | IE]>
                          </td>
                          </tr>
                          </table>
            
                          <table
                                  align="center" border="0" cellpadding="0" cellspacing="0" class="" style="width:600px;" width="600"
                          >
                              <tr>
                                  <td style="line-height:0px;font-size:0px;mso-line-height-rule:exactly;">
                          <![endif]-->
            
            
                          <div style="background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:600px;">
            
                              <table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation"
                                     style="background:#ffffff;background-color:#ffffff;width:100%;">
                                  <tbody>
                                  <tr>
                                      <td style="direction:ltr;font-size:0px;padding:1px 0px 1px 0px;text-align:center;">
                                          <!--[if mso | IE]>
                                          <table role="presentation" border="0" cellpadding="0" cellspacing="0">
            
                                              <tr>
            
                                                  <td
                                                          class="" style="vertical-align:top;width:150px;"
                                                  >
                                          <![endif]-->
            
                                          <div class="mj-column-per-25 outlook-group-fix"
                                               style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:25%;">
            
                                              <table border="0" cellpadding="0" cellspacing="0" role="presentation"
                                                     style="vertical-align:top;" width="100%">
            
                                                  <tr>
                                                      <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                                                          <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                              <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                                  <span style="font-size: 14px;"><b>Peso</b></span></p></div>
            
                                                      </td>
                                                  </tr>
            
                                                  <tr>
                                                      <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                                                          <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                              <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                                  <span style="font-size: 14px;">#PESO kg</span></p></div>
            
                                                      </td>
                                                  </tr>
            
                                              </table>
            
                                          </div>
            
                                          <!--[if mso | IE]>
                                          </td>
            
                                          <td
                                                  class="" style="vertical-align:top;width:150px;"
                                          >
                                          <![endif]-->
            
                                          <div class="mj-column-per-25 outlook-group-fix"
                                               style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:25%;">
            
                                              <table border="0" cellpadding="0" cellspacing="0" role="presentation"
                                                     style="vertical-align:top;" width="100%">
            
                                                  <tr>
                                                      <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                                                          <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                              <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                                  <span style="font-size: 14px;"><b>Largo</b></span></p></div>
            
                                                      </td>
                                                  </tr>
            
                                                  <tr>
                                                      <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                                                          <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                              <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                                  <span style="font-size: 14px;">#LARGO cm</span></p></div>
            
                                                      </td>
                                                  </tr>
            
                                              </table>
            
                                          </div>
            
                                          <!--[if mso | IE]>
                                          </td>
            
                                          <td
                                                  class="" style="vertical-align:top;width:150px;"
                                          >
                                          <![endif]-->
            
                                          <div class="mj-column-per-25 outlook-group-fix"
                                               style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:25%;">
            
                                              <table border="0" cellpadding="0" cellspacing="0" role="presentation"
                                                     style="vertical-align:top;" width="100%">
            
                                                  <tr>
                                                      <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                                                          <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                              <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                                  <span style="font-size: 14px;"><b>Ancho</b></span></p></div>
            
                                                      </td>
                                                  </tr>
            
                                                  <tr>
                                                      <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                                                          <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                              <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                                  <span style="font-size: 14px;">#ANCHO cm</span></p></div>
            
                                                      </td>
                                                  </tr>
            
                                              </table>
            
                                          </div>
            
                                          <!--[if mso | IE]>
                                          </td>
            
                                          <td
                                                  class="" style="vertical-align:top;width:150px;"
                                          >
                                          <![endif]-->
            
                                          <div class="mj-column-per-25 outlook-group-fix"
                                               style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:25%;">
            
                                              <table border="0" cellpadding="0" cellspacing="0" role="presentation"
                                                     style="vertical-align:top;" width="100%">
            
                                                  <tr>
                                                      <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                                                          <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                              <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                                  <span style="font-size: 14px;"><b>Alto</b></span></p></div>
            
                                                      </td>
                                                  </tr>
            
                                                  <tr>
                                                      <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                                                          <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                              <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                                  <span style="font-size: 14px;">#ALTO cm</span></p></div>
            
                                                      </td>
                                                  </tr>
            
                                              </table>
            
                                          </div>
            
                                          <!--[if mso | IE]>
                                          </td>
            
                                          </tr>
            
                                          </table>
                                          <![endif]-->
                                      </td>
                                  </tr>
                                  </tbody>
                              </table>
            
                          </div>
            
            
                          <!--[if mso | IE]>
                          </td>
                          </tr>
                          </table>
            
                          <table
                                  align="center" border="0" cellpadding="0" cellspacing="0" class="" style="width:600px;" width="600"
                          >
                              <tr>
                                  <td style="line-height:0px;font-size:0px;mso-line-height-rule:exactly;">
                          <![endif]-->
            
            
                          <div style="background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:600px;">
            
                              <table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation"
                                     style="background:#ffffff;background-color:#ffffff;width:100%;">
                                  <tbody>
                                  <tr>
                                      <td style="direction:ltr;font-size:0px;padding:1px 0px 1px 0px;text-align:center;">
                                          <!--[if mso | IE]>
                                          <table role="presentation" border="0" cellpadding="0" cellspacing="0">
            
                                              <tr>
            
                                                  <td
                                                          class="" style="vertical-align:top;width:300px;"
                                                  >
                                          <![endif]-->
            
                                          <div class="mj-column-per-50 outlook-group-fix"
                                               style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:50%;">
            
                                              <table border="0" cellpadding="0" cellspacing="0" role="presentation"
                                                     style="vertical-align:top;" width="100%">
            
                                                  <tr>
                                                      <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                                                          <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                              <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                                  <span style="font-size: 14px;"><b>Descripci&oacute;n</b></span></p></div>
            
                                                      </td>
                                                  </tr>
            
                                                  <tr>
                                                      <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                                                          <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                              <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                                  <span style="font-size: 14px;">#DESCRIPCION</span></p></div>
            
                                                      </td>
                                                  </tr>
            
                                              </table>
            
                                          </div>
            
                                          <!--[if mso | IE]>
                                          </td>
            
                                          <td
                                                  class="" style="vertical-align:top;width:300px;"
                                          >
                                          <![endif]-->
            
                                          <div class="mj-column-per-50 outlook-group-fix"
                                               style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:50%;">
            
                                              <table border="0" cellpadding="0" cellspacing="0" role="presentation"
                                                     style="vertical-align:top;" width="100%">
            
                                                  <tr>
                                                      <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                                                          <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                              <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: right;">
                                                                  <span style="font-size: 14px;"><b>Ctd</b></span></p></div>
            
                                                      </td>
                                                  </tr>
            
                                                  <tr>
                                                      <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                                                          <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                              <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: right;">
                                                                  <span style="font-size: 14px;">#CTD</span></p></div>
            
                                                      </td>
                                                  </tr>
            
                                              </table>
            
                                          </div>
            
                                          <!--[if mso | IE]>
                                          </td>
            
                                          </tr>
            
                                          </table>
                                          <![endif]-->
                                      </td>
                                  </tr>
                                  </tbody>
                              </table>
            
                          </div>
            
            
                          <!--[if mso | IE]>
                          </td>
                          </tr>
                          </table>
            
                          <table
                                  align="center" border="0" cellpadding="0" cellspacing="0" class="" style="width:600px;" width="600"
                          >
                              <tr>
                                  <td style="line-height:0px;font-size:0px;mso-line-height-rule:exactly;">
                          <![endif]-->
            
            
                          <div style="background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:600px;">
            
                              <table align="center" border="0" cellpadding="0" cellspacing="0" role="presentation"
                                     style="background:#ffffff;background-color:#ffffff;width:100%;">
                                  <tbody>
                                  <tr>
                                      <td style="direction:ltr;font-size:0px;padding:1px 0px 1px 0px;text-align:center;">
                                          <!--[if mso | IE]>
                                          <table role="presentation" border="0" cellpadding="0" cellspacing="0">
            
                                              <tr>
            
                                                  <td
                                                          class="" style="vertical-align:top;width:600px;"
                                                  >
                                          <![endif]-->
            
                                          <div class="mj-column-per-100 outlook-group-fix"
                                               style="font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%;">
            
                                              <table border="0" cellpadding="0" cellspacing="0" role="presentation"
                                                     style="vertical-align:top;" width="100%">
            
                                                  <tr>
                                                      <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                                                          <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                              <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                                  <span style="font-size: 14px;"><b>Observaciones</b></span></p></div>
            
                                                      </td>
                                                  </tr>
            
                                                  <tr>
                                                      <td align="left" style="font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;">
            
                                                          <div style="font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;">
                                                              <p style="font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;">
                                                                  <span style="font-size: 14px;">#OBSERVACIONES</span></p></div>
            
                                                      </td>
                                                  </tr>
            
                                              </table>
            
                                          </div>
            
                                          <!--[if mso | IE]>
                                          </td>
            
                                          </tr>
            
                                          </table>
                                          <![endif]-->
                                      </td>
                                  </tr>
                                  </tbody>
                              </table>
            
                          </div>
            
            
                          <!--[if mso | IE]>
                          </td>
                          </tr>
                          </table>
            
                          <table
                                  align="center" border="0" cellpadding="0" cellspacing="0" class="" style="width:600px;" width="600"
                          >
                              <tr>
                                  <td style="line-height:0px;font-size:0px;mso-line-height-rule:exactly;">
                          <![endif]-->
                      </div>""";
}
