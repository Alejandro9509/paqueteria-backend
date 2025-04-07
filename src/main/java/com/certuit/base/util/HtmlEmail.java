package com.certuit.base.util;

public class HtmlEmail {

    public static String htmlBody = "<!doctype html>\n" +
            "    <html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
            "      <head>\n" +
            "        <title>\n" +
            "\n" +
            "        </title>\n" +
            "        <!--[if !mso]><!-- -->\n" +
            "        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "        <!--<![endif]-->\n" +
            "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
            "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "        <style type=\"text/css\">\n" +
            "          #outlook a { padding:0; }\n" +
            "          body { margin:0;padding:0;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%; }\n" +
            "          table, td { border-collapse:collapse;mso-table-lspace:0pt;mso-table-rspace:0pt; }\n" +
            "          img { border:0;height:auto;line-height:100%; outline:none;text-decoration:none;-ms-interpolation-mode:bicubic; }\n" +
            "          p { display:block;margin:13px 0; }\n" +
            "        </style>\n" +
            "        <!--[if mso]>\n" +
            "        <xml>\n" +
            "        <o:OfficeDocumentSettings>\n" +
            "          <o:AllowPNG/>\n" +
            "          <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
            "        </o:OfficeDocumentSettings>\n" +
            "        </xml>\n" +
            "        <![endif]-->\n" +
            "        <!--[if lte mso 11]>\n" +
            "        <style type=\"text/css\">\n" +
            "\n" +
            "\n" +
            "          .outlook-group-fix { width:100% !important; }\n" +
            "        </style>\n" +
            "        <![endif]-->\n" +
            "\n" +
            "      <!--[if !mso]><!-->\n" +
            "        <link href=\"https://fonts.googleapis.com/css?family=Ubuntu:300,400,500,700\" rel=\"stylesheet\" type=\"text/css\">\n" +
            "        <link href=\"http://localhost:8091/css/main.08c24f53.scss\" rel=\"stylesheet\">\n" +
            "        <style type=\"text/css\">\n" +
            "          @import url(https://fonts.googleapis.com/css?family=Ubuntu:300,400,500,700);\n" +
            "        </style>\n" +
            "      <!--<![endif]-->\n" +
            "\n" +
            "\n" +
            "    <style type=\"text/css\">\n" +
            "      @media only screen and (max-width:480px) {\n" +
            "        .mj-column-per-66 { width:66.66666666666666% !important; max-width: 66.66666666666666%; }\n" +
            ".mj-column-per-33 { width:33.33333333333333% !important; max-width: 33.33333333333333%; }\n" +
            ".mj-column-per-25 { width:25% !important; max-width: 25%; }\n" +
            ".mj-column-per-75 { width:75% !important; max-width: 75%; }\n" +
            ".mj-column-per-100 { width:100% !important; max-width: 100%; }\n" +
            ".mj-column-per-50 { width:50% !important; max-width: 50%; }\n" +
            "      }\n" +
            "    </style>\n" +
            "\n" +
            "\n" +
            "        <style type=\"text/css\">\n" +
            "\n" +
            "\n" +
            "\n" +
            "    @media only screen and (max-width:480px) {\n" +
            "      table.full-width-mobile { width: 100% !important; }\n" +
            "      td.full-width-mobile { width: auto !important; }\n" +
            "    }\n" +
            "\n" +
            "        </style>\n" +
            "        <style type=\"text/css\">.hide_on_mobile { display: none !important;}\n" +
            "        @media only screen and (min-width: 480px) { .hide_on_mobile { display: block !important;} }\n" +
            "        .hide_section_on_mobile { display: none !important;}\n" +
            "        @media only screen and (min-width: 480px) {\n" +
            "            .hide_section_on_mobile {\n" +
            "                display: table !important;\n" +
            "            }\n" +
            "\n" +
            "            div.hide_section_on_mobile {\n" +
            "                display: block !important;\n" +
            "            }\n" +
            "        }\n" +
            "        .hide_on_desktop { display: block !important;}\n" +
            "        @media only screen and (min-width: 480px) { .hide_on_desktop { display: none !important;} }\n" +
            "        .hide_section_on_desktop {\n" +
            "            display: table !important;\n" +
            "            width: 100%;\n" +
            "        }\n" +
            "        @media only screen and (min-width: 480px) { .hide_section_on_desktop { display: none !important;} }\n" +
            "\n" +
            "          p, h1, h2, h3 {\n" +
            "              margin: 0px;\n" +
            "          }\n" +
            "\n" +
            "          ul, li, ol {\n" +
            "            font-size: 11px;\n" +
            "            font-family: Ubuntu, Helvetica, Arial;\n" +
            "          }\n" +
            "\n" +
            "          a {\n" +
            "              text-decoration: none;\n" +
            "              color: inherit;\n" +
            "          }\n" +
            "\n" +
            "          @media only screen and (max-width:480px) {\n" +
            "\n" +
            "            .mj-column-per-100 { width:100%!important; max-width:100%!important; }\n" +
            "            .mj-column-per-100 > .mj-column-per-75 { width:75%!important; max-width:75%!important; }\n" +
            "            .mj-column-per-100 > .mj-column-per-60 { width:60%!important; max-width:60%!important; }\n" +
            "            .mj-column-per-100 > .mj-column-per-50 { width:50%!important; max-width:50%!important; }\n" +
            "            .mj-column-per-100 > .mj-column-per-40 { width:40%!important; max-width:40%!important; }\n" +
            "            .mj-column-per-100 > .mj-column-per-33 { width:33.333333%!important; max-width:33.333333%!important; }\n" +
            "            .mj-column-per-100 > .mj-column-per-25 { width:25%!important; max-width:25%!important; }\n" +
            "\n" +
            "            .mj-column-per-100 { width:100%!important; max-width:100%!important; }\n" +
            "            .mj-column-per-75 { width:100%!important; max-width:100%!important; }\n" +
            "            .mj-column-per-60 { width:100%!important; max-width:100%!important; }\n" +
            "            .mj-column-per-50 { width:100%!important; max-width:100%!important; }\n" +
            "            .mj-column-per-40 { width:100%!important; max-width:100%!important; }\n" +
            "            .mj-column-per-33 { width:100%!important; max-width:100%!important; }\n" +
            "            .mj-column-per-25 { width:100%!important; max-width:100%!important; }\n" +
            "        }\n" +
            "        @import \"https://fonts.googleapis.com/css?family=Montserrat:400,700|Raleway:300,400\";\n" +
            "        /* colors */\n" +
            "        /* tab setting */\n" +
            "        /* breakpoints */\n" +
            "        /* selectors relative to radio inputs */\n" +
            "        html {\n" +
            "            width: 100%;\n" +
            "            height: 100%;\n" +
            "        }\n" +
            "\n" +
            "\n" +
            "        body {\n" +
            "            background: #efefef;\n" +
            "            color: #333;\n" +
            "            font-family: \"Raleway\";\n" +
            "            height: 100%;\n" +
            "        }\n" +
            "        body h1 {\n" +
            "            text-align: center;\n" +
            "            color: #F9A03E;\n" +
            "            font-weight: 300;\n" +
            "            padding: 40px 0 20px 0;\n" +
            "            margin: 0;\n" +
            "        }\n" +
            "\n" +
            "        .tabs {\n" +
            "            left: 50% !important;\n" +
            "            transform: translateX(-50%) !important;\n" +
            "            position: relative !important;\n" +
            "            background: white !important;\n" +
            "            padding-bottom: 20px !important;\n" +
            "            border-radius: 5px !important;\n" +
            "            min-width: 240px !important;\n" +
            "        }\n" +
            "        .tabs input[name=tab-control] {\n" +
            "            display: none !important;\n" +
            "        }\n" +
            "        .tabs .content section h2,\n" +
            "        .tabs ul li label {\n" +
            "            font-family: \"Montserrat\";\n" +
            "            font-weight: bold;\n" +
            "            font-size: 18px;\n" +
            "            color: #F9A03E;\n" +
            "        }\n" +
            "        .tabs ul {\n" +
            "            list-style-type: none !important;\n" +
            "            padding-left: 0;\n" +
            "            display: flex !important;\n" +
            "            flex-direction: row !important;\n" +
            "            margin-bottom: 10px;\n" +
            "            justify-content: space-between !important;\n" +
            "            align-items: flex-end !important;\n" +
            "            flex-wrap: wrap !important;\n" +
            "        }\n" +
            "        .tabs ul li {\n" +
            "            box-sizing: border-box !important;\n" +
            "            flex: 1 !important;\n" +
            "            width: 50% !important;\n" +
            "            padding: 0 10px !important;\n" +
            "            text-align: center !important;\n" +
            "        }\n" +
            "        .tabs ul li label {\n" +
            "            transition: all 0.3s ease-in-out !important;\n" +
            "            color: #929daf !important;\n" +
            "            padding: 5px auto !important;\n" +
            "            overflow: hidden !important;\n" +
            "            text-overflow: ellipsis !important;\n" +
            "            display: block !important;\n" +
            "            cursor: pointer !important;\n" +
            "            transition: all 0.2s ease-in-out !important;\n" +
            "            white-space: nowrap !important;\n" +
            "            -webkit-touch-callout: none !important;\n" +
            "            -webkit-user-select: none !important;\n" +
            "            -moz-user-select: none !important;\n" +
            "            -ms-user-select: none !important;\n" +
            "            user-select: none !important;\n" +
            "        }\n" +
            "        .tabs ul li label br {\n" +
            "            display: none !important;\n" +
            "        }\n" +
            "        .tabs ul li label svg {\n" +
            "            fill: #929daf !important;\n" +
            "            height: 1.2em !important;\n" +
            "            vertical-align: bottom !important;\n" +
            "            margin-right: 0.2em !important;\n" +
            "            transition: all 0.2s ease-in-out !important;\n" +
            "        }\n" +
            "        .tabs ul li label:hover, .tabs ul li label:focus, .tabs ul li label:active {\n" +
            "            outline: 0 !important;\n" +
            "            color: #bec5cf !important;\n" +
            "        }\n" +
            "        .tabs ul li label:hover svg, .tabs ul li label:focus svg, .tabs ul li label:active svg {\n" +
            "            fill: #bec5cf !important;\n" +
            "        }\n" +
            "        .tabs .slider {\n" +
            "            position: relative !important;\n" +
            "            width: 50% !important;\n" +
            "            transition: all 0.33s cubic-bezier(0.38, 0.8, 0.32, 1.07) !important;\n" +
            "        }\n" +
            "        .tabs .slider .indicator {\n" +
            "            position: relative !important;\n" +
            "            width: 50px !important;\n" +
            "            max-width: 100% !important;\n" +
            "            margin: 0 auto !important;\n" +
            "            height: 4px !important;\n" +
            "            background: #F9A03E !important;\n" +
            "            border-radius: 1px !important;\n" +
            "        }\n" +
            "        .tabs .content {\n" +
            "            margin-top: 30px !important;\n" +
            "        }\n" +
            "        .tabs .content section {\n" +
            "            display: none !important;\n" +
            "            -webkit-animation-name: content !important;\n" +
            "            animation-name: content !important;\n" +
            "            -webkit-animation-direction: normal !important;\n" +
            "            animation-direction: normal !important;\n" +
            "            -webkit-animation-duration: 0.3s !important;\n" +
            "            animation-duration: 0.3s !important;\n" +
            "            -webkit-animation-timing-function: ease-in-out !important;\n" +
            "            animation-timing-function: ease-in-out !important;\n" +
            "            -webkit-animation-iteration-count: 1 !important;\n" +
            "            animation-iteration-count: 1 !important;\n" +
            "            line-height: 1.4 !important;\n" +
            "        }\n" +
            "        .tabs .content section h2 {\n" +
            "            color: #F9A03E !important;\n" +
            "            display: none !important;\n" +
            "        }\n" +
            "        .tabs .content section h2::after {\n" +
            "            content: \"\" !important;\n" +
            "            position: relative !important;\n" +
            "            display: block !important;\n" +
            "            width: 30px !important;\n" +
            "            height: 3px !important;\n" +
            "            background: #F9A03E !important;\n" +
            "            margin-top: 5px !important;\n" +
            "            left: 1px !important;\n" +
            "        }\n" +
            "        .tabs input[name=tab-control]:nth-of-type(1):checked ~ ul > li:nth-child(1) > label {\n" +
            "            cursor: default!important;\n" +
            "            color: #F9A03E !important;\n" +
            "        }\n" +
            "        .tabs input[name=tab-control]:nth-of-type(1):checked ~ ul > li:nth-child(1) > label svg {\n" +
            "            fill: #F9A03E !important;\n" +
            "        }\n" +
            "        @media (max-width: 600px) {\n" +
            "            .tabs input[name=tab-control]:nth-of-type(1):checked ~ ul > li:nth-child(1) > label {\n" +
            "                background: rgba(0, 0, 0, 0.08) !important;\n" +
            "            }\n" +
            "        }\n" +
            "        .tabs input[name=tab-control]:nth-of-type(1):checked ~ .slider {\n" +
            "            transform: translateX(0%) !important;\n" +
            "        }\n" +
            "        .tabs input[name=tab-control]:nth-of-type(1):checked ~ .content > section:nth-child(1) {\n" +
            "            display: block !important;\n" +
            "        }\n" +
            "        .tabs input[name=tab-control]:nth-of-type(2):checked ~ ul > li:nth-child(2) > label {\n" +
            "            cursor: default !important;\n" +
            "            color: #F9A03E !important;\n" +
            "        }\n" +
            "        .tabs input[name=tab-control]:nth-of-type(2):checked ~ ul > li:nth-child(2) > label svg {\n" +
            "            fill: #F9A03E !important;\n" +
            "        }\n" +
            "        @media (max-width: 600px) {\n" +
            "            .tabs input[name=tab-control]:nth-of-type(2):checked ~ ul > li:nth-child(2) > label {\n" +
            "                background: rgba(0, 0, 0, 0.08) !important;\n" +
            "            }\n" +
            "        }\n" +
            "        .tabs input[name=tab-control]:nth-of-type(2):checked ~ .slider {\n" +
            "            transform: translateX(100%) !important;\n" +
            "        }\n" +
            "        .tabs input[name=tab-control]:nth-of-type(2):checked ~ .content > section:nth-child(2) {\n" +
            "            display: block !important;\n" +
            "        }\n" +
            "        .tabs input[name=tab-control]:nth-of-type(3):checked ~ ul > li:nth-child(3) > label {\n" +
            "            cursor: default !important;\n" +
            "            color: #F9A03E !important;\n" +
            "        }\n" +
            "        .tabs input[name=tab-control]:nth-of-type(3):checked ~ ul > li:nth-child(3) > label svg {\n" +
            "            fill: #F9A03E !important;\n" +
            "        }\n" +
            "        @media (max-width: 600px) {\n" +
            "            .tabs input[name=tab-control]:nth-of-type(3):checked ~ ul > li:nth-child(3) > label {\n" +
            "                background: rgba(0, 0, 0, 0.08) !important;\n" +
            "            }\n" +
            "        }\n" +
            "        .tabs input[name=tab-control]:nth-of-type(3):checked ~ .slider {\n" +
            "            transform: translateX(200%) !important;\n" +
            "        }\n" +
            "        .tabs input[name=tab-control]:nth-of-type(3):checked ~ .content > section:nth-child(3) {\n" +
            "            display: block !important;\n" +
            "        }\n" +
            "        .tabs input[name=tab-control]:nth-of-type(4):checked ~ ul > li:nth-child(4) > label {\n" +
            "            cursor: default !important;\n" +
            "            color: #F9A03E !important;\n" +
            "        }\n" +
            "        .tabs input[name=tab-control]:nth-of-type(4):checked ~ ul > li:nth-child(4) > label svg {\n" +
            "            fill: #F9A03E !important;\n" +
            "        }\n" +
            "        @media (max-width: 600px) {\n" +
            "            .tabs input[name=tab-control]:nth-of-type(4):checked ~ ul > li:nth-child(4) > label {\n" +
            "                background: rgba(0, 0, 0, 0.08) !important;\n" +
            "            }\n" +
            "        }\n" +
            "        .tabs input[name=tab-control]:nth-of-type(4):checked ~ .slider {\n" +
            "            transform: translateX(300%) !important;\n" +
            "        }\n" +
            "        .tabs input[name=tab-control]:nth-of-type(4):checked ~ .content > section:nth-child(4) {\n" +
            "            display: block !important;\n" +
            "        }\n" +
            "        @-webkit-keyframes content {\n" +
            "            from {\n" +
            "                opacity: 0 !important;\n" +
            "                transform: translateY(5%) !important;\n" +
            "            }\n" +
            "            to {\n" +
            "                opacity: 1 !important;\n" +
            "                transform: translateY(0%) !important;\n" +
            "            }\n" +
            "        }\n" +
            "        @keyframes content {\n" +
            "            from {\n" +
            "                opacity: 0 !important;\n" +
            "                transform: translateY(5%) !important;\n" +
            "            }\n" +
            "            to {\n" +
            "                opacity: 1 !important;\n" +
            "                transform: translateY(0%) !important;\n" +
            "            }\n" +
            "        }\n" +
            "        @media (max-width: 1000px) {\n" +
            "            .tabs ul li label {\n" +
            "                white-space: initial !important;\n" +
            "            }\n" +
            "            .tabs ul li label br {\n" +
            "                display: initial !important;\n" +
            "            }\n" +
            "            .tabs ul li label svg {\n" +
            "                height: 1.5em !important;\n" +
            "            }\n" +
            "        }\n" +
            "        @media (max-width: 600px) {\n" +
            "            .tabs ul li label {\n" +
            "                padding: 5px !important;\n" +
            "                border-radius: 5px !important;\n" +
            "            }\n" +
            "            .tabs ul li label span {\n" +
            "                display: none !important;\n" +
            "            }\n" +
            "            .tabs .slider {\n" +
            "                display: none !important;\n" +
            "            }\n" +
            "            .tabs .content {\n" +
            "                margin-top: 20px !important;\n" +
            "            }\n" +
            "            .tabs .content section h2 {\n" +
            "                display: block !important;\n" +
            "            }\n" +
            "        }\n" +
            "\n" +
            "        .history-tl-container{\n" +
            "            font-family: \"Roboto\",sans-serif !important;\n" +
            "            width:50% !important;\n" +
            "            margin:auto !important;\n" +
            "            display:block !important;\n" +
            "            position:relative !important;\n" +
            "        }\n" +
            "        .history-tl-container ul.tl{\n" +
            "            margin:20px 0 !important;\n" +
            "            padding:0 !important;\n" +
            "            display:inline-block !important;\n" +
            "\n" +
            "        }\n" +
            "        .history-tl-container ul.tl li{\n" +
            "            list-style: none !important;\n" +
            "            margin:auto !important;\n" +
            "            margin-left:200px !important;\n" +
            "            min-height:50px !important;\n" +
            "            /*background: rgba(255,255,0,0.1);*/\n" +
            "            border-left:1px dashed #86D6FF !important;\n" +
            "            padding:0 0 50px 30px !important;\n" +
            "            position:relative !important;\n" +
            "        }\n" +
            "        .history-tl-container ul.tl li:last-child{ border-left:0;}\n" +
            "        .history-tl-container ul.tl li::before{\n" +
            "            position: absolute !important;\n" +
            "            left: -18px !important;\n" +
            "            top: -5px !important;\n" +
            "            content: \" \" !important;\n" +
            "            border: 8px solid rgba(255, 255, 255, 0.74) !important;\n" +
            "            border-radius: 500% !important;\n" +
            "            background: #F9A03E !important;\n" +
            "            height: 20px !important;\n" +
            "            width: 20px !important;\n" +
            "            transition: all 500ms ease-in-out !important;\n" +
            "\n" +
            "        }\n" +
            "        .history-tl-container ul.tl li:hover::before{\n" +
            "            border-color:  #F9A03E !important;\n" +
            "            transition: all 1000ms ease-in-out !important;\n" +
            "        }\n" +
            "        ul.tl li .item-title{\n" +
            "        }\n" +
            "        ul.tl li .item-detail{\n" +
            "            color:rgba(0,0,0,0.5) !important;\n" +
            "            font-size:12px !important;\n" +
            "        }\n" +
            "        ul.tl li .timestamp{\n" +
            "            color: #8D8D8D !important;\n" +
            "            position: absolute !important;\n" +
            "            width:100px !important;\n" +
            "            left: -75% !important;\n" +
            "            text-align: right !important;\n" +
            "            font-size: 12px !important;\n" +
            "        }\n" +
            "\n" +
            "        .vcv-timeline {\n" +
            "            position: relative !important;\n" +
            "            display: flex !important;\n" +
            "            align-items: center !important;\n" +
            "            max-width: 930px !important;\n" +
            "            margin: 0 auto !important;\n" +
            "            padding: 0 !important;\n" +
            "            list-style-type: none !important;\n" +
            "            color: #a7a7a7 !important;\n" +
            "        }\n" +
            "        .vcv-timeline-item {\n" +
            "            display: flex !important;\n" +
            "            align-items: center !important;\n" +
            "            flex: 1 0 auto !important;\n" +
            "            padding: 0 !important;\n" +
            "        }\n" +
            "        .vcv-timeline-item::before,\n" +
            "        .vcv-timeline-item::after {\n" +
            "            content: '' !important;\n" +
            "        }\n" +
            "        .vcv-timeline-item::before {\n" +
            "            content: attr(data-step) !important;\n" +
            "            display: inline-flex !important;\n" +
            "            justify-content: center !important;\n" +
            "            align-items: center !important;\n" +
            "            flex: 0 0 32px !important;\n" +
            "            height: 32px !important;\n" +
            "            margin: 0 10px 0 0 !important;\n" +
            "            border-radius: 50% !important;\n" +
            "            border: 2px solid #a7a7a7 !important;\n" +
            "            color: #a7a7a7 !important;\n" +
            "        }\n" +
            "        .vcv-timeline-item::after {\n" +
            "            height: 2px !important;\n" +
            "            background: #a7a7a7 !important;\n" +
            "            width: 100% !important;\n" +
            "            margin: 0 10px !important;\n" +
            "        }\n" +
            "        .vcv-timeline-item:last-of-type {\n" +
            "            flex: 0 0 120px !important;\n" +
            "        }\n" +
            "        .vcv-timeline-item:last-of-type::after {\n" +
            "            display: none !important;\n" +
            "        }\n" +
            "        .vcv-timeline-item.vcv-step-done::before {\n" +
            "            content: '' !important;\n" +
            "            border-color: #aace35 !important;\n" +
            "            background: #aace35 url(\"data:image/svg+xml,%0A%3Csvg width='14px' height='11px' viewBox='0 0 14 11' version='1.1' xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink'%3E%3Cg id='Page-1' stroke='none' stroke-width='1' fill='none' fill-rule='evenodd'%3E%3Cg id='Activation-Theme-to-Premium' transform='translate(-827.000000, -183.000000)' fill='%23FFFFFF' fill-rule='nonzero'%3E%3Cg id='Activation-02' transform='translate(342.000000, 172.000000)'%3E%3Cg id='Navigation' transform='translate(44.000000, 0.000000)'%3E%3Cg id='check' transform='translate(441.000000, 11.000000)'%3E%3Cpolygon id='Path' points='11.8606017 0 4.86058121 6.82615443 2.13952103 4.17292819 0 6.25973131 4.8606221 11 14 2.08684301'%3E%3C/polygon%3E%3C/g%3E%3C/g%3E%3C/g%3E%3C/g%3E%3C/g%3E%3C/svg%3E\") no-repeat center !important;\n" +
            "            background-size: 40% !important;\n" +
            "        }\n" +
            "        .vcv-timeline-item.vcv-step-done::after {\n" +
            "            background: #aace35 !important;\n" +
            "        }\n" +
            "        @media screen and (max-width: 768px) {\n" +
            "            .vcv-timeline {\n" +
            "                margin-bottom: 50px !important;\n" +
            "            }\n" +
            "            .vcv-timeline-item {\n" +
            "                position: relative !important;\n" +
            "            }\n" +
            "            .vcv-timeline-item span {\n" +
            "                text-align: left !important;\n" +
            "                margin: 10px 0 0 !important;\n" +
            "                position: absolute !important;\n" +
            "                top: 30px !important;\n" +
            "                width: 100% !important;\n" +
            "                left: 2px !important;\n" +
            "            }\n" +
            "            .vcv-timeline-item:last-of-type {\n" +
            "                flex: 0 0 auto !important;\n" +
            "                margin: 0 30px 0 0 !important;\n" +
            "            }\n" +
            "            .vcv-timeline-item::before {\n" +
            "                margin: 0 !important;\n" +
            "            }\n" +
            "            .vcv-timeline-item::after {\n" +
            "                flex: 0 1 auto !important;\n" +
            "            }\n" +
            "        }\n" +
            "        </style>\n" +
            "\n" +
            "      </head>\n" +
            "      <body style=\"background-color:#f0f0f0;\">\n" +
            "\n" +
            "\n" +
            "      <div style=\"background-color:#f0f0f0;\">\n" +
            "\n" +
            "      <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background:#f0f0f0;background-color:#f0f0f0;width:100%;\">\n" +
            "        <tbody>\n" +
            "          <tr>\n" +
            "            <td>\n" +
            "\n" +
            "\n" +
            "      <!--[if mso | IE]>\n" +
            "      <table\n" +
            "         align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n" +
            "      >\n" +
            "        <tr>\n" +
            "          <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
            "      <![endif]-->\n" +
            "\n" +
            "\n" +
            "      <div style=\"margin:0px auto;max-width:80%;\">\n" +
            "\n" +
            "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"width:100%;\">\n" +
            "          <tbody>\n" +
            "            <tr>\n" +
            "              <td style=\"direction:ltr;font-size:0px;padding:5px 0px 5px 0px;text-align:center;\">\n" +
            "                <!--[if mso | IE]>\n" +
            "                  <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "\n" +
            "        <tr>\n" +
            "\n" +
            "            <td\n" +
            "               class=\"\" style=\"vertical-align:middle;width:399.99999999999994px;\"\n" +
            "            >\n" +
            "          <![endif]-->\n" +
            "\n" +
            "      <div class=\"mj-column-per-66 outlook-group-fix\" style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:middle;width:66.66666666666666%;\">\n" +
            "\n" +
            "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align:middle;\" width=\"100%\">\n" +
            "\n" +
            "            <tr>\n" +
            "              <td style=\"font-size:0px;word-break:break-word;\">\n" +
            "\n" +
            "\n" +
            "    <!--[if mso | IE]>\n" +
            "\n" +
            "        <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td height=\"10\" style=\"vertical-align:top;height:10px;\">\n" +
            "\n" +
            "    <![endif]-->\n" +
            "\n" +
            "      <div style=\"height:10px;\">\n" +
            "        &nbsp;\n" +
            "      </div>\n" +
            "\n" +
            "    <!--[if mso | IE]>\n" +
            "\n" +
            "        </td></tr></table>\n" +
            "\n" +
            "    <![endif]-->\n" +
            "\n" +
            "\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "\n" +
            "      </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "          <!--[if mso | IE]>\n" +
            "            </td>\n" +
            "\n" +
            "            <td\n" +
            "               class=\"\" style=\"vertical-align:middle;width:199.99999999999997px;\"\n" +
            "            >\n" +
            "          <![endif]-->\n" +
            "\n" +
            "      <div class=\"mj-column-per-33 outlook-group-fix\" style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:middle;width:33.33333333333333%;\">\n" +
            "\n" +
            "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align:middle;\" width=\"100%\">\n" +
            "\n" +
            "            <tr>\n" +
            "              <td style=\"font-size:0px;word-break:break-word;\">\n" +
            "\n" +
            "\n" +
            "    <!--[if mso | IE]>\n" +
            "\n" +
            "        <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td height=\"10\" style=\"vertical-align:top;height:10px;\">\n" +
            "\n" +
            "    <![endif]-->\n" +
            "\n" +
            "      <div style=\"height:10px;\">\n" +
            "        &nbsp;\n" +
            "      </div>\n" +
            "\n" +
            "    <!--[if mso | IE]>\n" +
            "\n" +
            "        </td></tr></table>\n" +
            "\n" +
            "    <![endif]-->\n" +
            "\n" +
            "\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "\n" +
            "      </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "          <!--[if mso | IE]>\n" +
            "            </td>\n" +
            "\n" +
            "        </tr>\n" +
            "\n" +
            "                  </table>\n" +
            "                <![endif]-->\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "          </tbody>\n" +
            "        </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "\n" +
            "      <!--[if mso | IE]>\n" +
            "          </td>\n" +
            "        </tr>\n" +
            "      </table>\n" +
            "      <![endif]-->\n" +
            "\n" +
            "\n" +
            "            </td>\n" +
            "          </tr>\n" +
            "        </tbody>\n" +
            "      </table>\n" +
            "\n" +
            "\n" +
            "      <!--[if mso | IE]>\n" +
            "      <table\n" +
            "         align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n" +
            "      >\n" +
            "        <tr>\n" +
            "          <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
            "      <![endif]-->\n" +
            "\n" +
            "\n" +
            "      <div style=\"background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:80%;\">\n" +
            "\n" +
            "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background:#ffffff;background-color:#ffffff;width:100%;\">\n" +
            "          <tbody>\n" +
            "            <tr>\n" +
            "              <td style=\"direction:ltr;font-size:0px;padding:9px 0px 0px 0px;text-align:center;\">\n" +
            "                <!--[if mso | IE]>\n" +
            "                  <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "\n" +
            "        <tr>\n" +
            "\n" +
            "            <td\n" +
            "               class=\"\" style=\"vertical-align:top;width:150px;\"\n" +
            "            >\n" +
            "          <![endif]-->\n" +
            "\n" +
            "      <div class=\"mj-column-per-25 outlook-group-fix\" style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:25%;\">\n" +
            "\n" +
            "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align:top;\" width=\"100%\">\n" +
            "\n" +
            "            <tr>\n" +
            "              <td align=\"right\" style=\"font-size:0px;padding:0px 0px 0px 0px;word-break:break-word;\">\n" +
            "\n" +
            "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"border-collapse:collapse;border-spacing:0px;\">\n" +
            "        <tbody>\n" +
            "          <tr>\n" +
            "            <td style=\"width:60px;\">\n" +
            "\n" +
            "      <img height=\"auto\" src=\"https://s3-eu-west-1.amazonaws.com/topolio/uploads/61846b9f99fca/1636070646.jpg\" style=\"border:0;display:block;outline:none;text-decoration:none;height:auto;width:100%;font-size:13px;\" width=\"60\">\n" +
            "\n" +
            "            </td>\n" +
            "          </tr>\n" +
            "        </tbody>\n" +
            "      </table>\n" +
            "\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "\n" +
            "      </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "          <!--[if mso | IE]>\n" +
            "            </td>\n" +
            "\n" +
            "            <td\n" +
            "               class=\"\" style=\"vertical-align:top;width:450px;\"\n" +
            "            >\n" +
            "          <![endif]-->\n" +
            "\n" +
            "      <div class=\"mj-column-per-75 outlook-group-fix\" style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:75%;\">\n" +
            "\n" +
            "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align:top;\" width=\"100%\">\n" +
            "\n" +
            "            <tr>\n" +
            "              <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "      <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\"><p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\"><span style=\"font-size: 20px;\">Hola, #CLIENTE</span></p></div>\n" +
            "\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "\n" +
            "      </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "          <!--[if mso | IE]>\n" +
            "            </td>\n" +
            "\n" +
            "        </tr>\n" +
            "\n" +
            "                  </table>\n" +
            "                <![endif]-->\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "          </tbody>\n" +
            "        </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "\n" +
            "      <!--[if mso | IE]>\n" +
            "          </td>\n" +
            "\n" +
            "        </tr>\n" +
            "      </table>\n" +
            "\n" +
            "      <table\n" +
            "         align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n" +
            "      >\n" +
            "        <tr>\n" +
            "          <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
            "      <![endif]-->\n" +
            "\n" +
            "\n" +
            "      <div style=\"background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:80%;\">\n" +
            "\n" +
            "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background:#ffffff;background-color:#ffffff;width:100%;\">\n" +
            "          <tbody>\n" +
            "            <tr>\n" +
            "              <td style=\"direction:ltr;font-size:0px;padding:0px 0px 0px 0px;text-align:center;\">\n" +
            "                <!--[if mso | IE]>\n" +
            "                  <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "\n" +
            "        <tr>\n" +
            "\n" +
            "            <td\n" +
            "               class=\"\" style=\"vertical-align:middle;width:600px;\"\n" +
            "            >\n" +
            "          <![endif]-->\n" +
            "\n" +
            "      <div class=\"mj-column-per-100 outlook-group-fix\" style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:middle;width:100%;\">\n" +
            "\n" +
            "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align:middle;\" width=\"100%\">\n" +
            "          <tr>\n" +
            "              <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "                  <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\"><p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: center;\"><span style=\"font-size: 18px;\"> #MENSAJE</span></p></div>\n" +
            "\n" +
            "              </td>\n" +
            "          </tr>\n" +
            "            <tr>\n" +
            "              <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "      <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\"><p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: center;\"><span style=\"font-size: 18px;\"><span style=\"color: #f9a03e;\">N&uacute;mero de rastreo:</span> #TRACKING</span></p></div>\n" +
            "\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "\n" +
            "            <tr>\n" +
            "              <td align=\"center\" vertical-align=\"middle\" style=\"font-size:0px;padding:0px 1px 0px 0px;padding-top:10;padding-bottom:45;word-break:break-word;\">\n" +
            "\n" +
            "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"border-collapse:separate;line-height:100%;\">\n" +
            "        <tr>\n" +
            "          <td align=\"center\" bgcolor=\"#F9A03E\" role=\"presentation\" style=\"border:none;border-radius:8px;cursor:auto;mso-padding-alt:8px 26px 9px 26px;background:#F9A03E;\" valign=\"middle\">\n" +
            "            <a href=\"#LINK_TRACKING\" style=\"display: inline-block; background: #F9A03E; color: #fffff; font-family: Ubuntu, Helvetica, Arial, sans-serif, Helvetica, Arial, sans-serif; font-size: 20px; font-weight: normal; line-height: 25px; margin: 0; text-decoration: none; text-transform: none; padding: 8px 26px 9px 26px; mso-padding-alt: 0px; border-radius: 8px;\" target=\"_blank\">\n" +
            "              <span style=\"font-size: 20px;\">Rastrear Paquete</span>\n" +
            "            </a>\n" +
            "          </td>\n" +
            "        </tr>\n" +
            "      </table>\n" +
            "\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "\n" +
            "      </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "          <!--[if mso | IE]>\n" +
            "            </td>\n" +
            "\n" +
            "        </tr>\n" +
            "\n" +
            "                  </table>\n" +
            "                <![endif]-->\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "          </tbody>\n" +
            "        </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "\n" +
            "      <!--[if mso | IE]>\n" +
            "          </td>\n" +
            "        </tr>\n" +
            "      </table>\n" +
            "\n" +
            "      <table\n" +
            "         align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n" +
            "      >\n" +
            "        <tr>\n" +
            "          <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
            "      <![endif]-->\n" +
            "\n" +
            "\n" +
            "      <div style=\"background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:80%;\">\n" +
            "\n" +
            "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background:#ffffff;background-color:#ffffff;width:100%;\">\n" +
            "          <tbody>\n" +
            "            <tr>\n" +
            "              <td style=\"direction:ltr;font-size:0px;padding:2px 0px 2px 0px;text-align:center;\">\n" +
            "                <!--[if mso | IE]>\n" +
            "                  <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "\n" +
            "        <tr>\n" +
            "\n" +
            "            <td\n" +
            "               class=\"\" style=\"vertical-align:top;width:600px;\"\n" +
            "            >\n" +
            "          <![endif]-->\n" +
            "\n" +
            "      <div class=\"mj-column-per-100 outlook-group-fix\" style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%;\">\n" +
            "\n" +
            "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align:top;\" width=\"100%\">\n" +
            "\n" +
            "            <tr>\n" +
            "              <td style=\"font-size:0px;padding:0px 5px;padding-top:5px;word-break:break-word;\">\n" +
            "\n" +
            "      <p style=\"font-family: Ubuntu, Helvetica, Arial; border-top: solid 2px #F9A03E; font-size: 1; margin: 0px auto; width: 100%;\">\n" +
            "      </p>\n" +
            "\n" +
            "      <!--[if mso | IE]>\n" +
            "        <table\n" +
            "           align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-top:solid 2px #F9A03E;font-size:1;margin:0px auto;width:580px;\" role=\"presentation\" width=\"580px\"\n" +
            "        >\n" +
            "          <tr>\n" +
            "            <td style=\"height:0;line-height:0;\">\n" +
            "              &nbsp;\n" +
            "            </td>\n" +
            "          </tr>\n" +
            "        </table>\n" +
            "      <![endif]-->\n" +
            "\n" +
            "\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "\n" +
            "      </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "          <!--[if mso | IE]>\n" +
            "            </td>\n" +
            "\n" +
            "        </tr>\n" +
            "\n" +
            "                  </table>\n" +
            "                <![endif]-->\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "          </tbody>\n" +
            "        </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "\n" +
            "      <!--[if mso | IE]>\n" +
            "          </td>\n" +
            "        </tr>\n" +
            "      </table>\n" +
            "\n" +
            "      <table\n" +
            "         align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n" +
            "      >\n" +
            "        <tr>\n" +
            "          <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
            "      <![endif]-->\n" +
            "\n" +
            "\n" +
            "      <div style=\"background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:80%;\">\n" +
            "\n" +
            "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background:#ffffff;background-color:#ffffff;width:100%;\">\n" +
            "          <tbody>\n" +
            "            <tr>\n" +
            "              <td style=\"direction:ltr;font-size:0px;padding:0px 0px 0px 0px;text-align:center;\">\n" +
            "                <!--[if mso | IE]>\n" +
            "                  <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "\n" +
            "        <tr>\n" +
            "\n" +
            "            <td\n" +
            "               class=\"\" style=\"vertical-align:top;width:300px;\"\n" +
            "            >\n" +
            "          <![endif]-->\n" +
            "\n" +
            "      <div class=\"mj-column-per-50 outlook-group-fix\" style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:50%;\">\n" +
            "\n" +
            "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align:top;\" width=\"100%\">\n" +
            "\n" +
            "            <tr>\n" +
            "              <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "      <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\"><p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: center;\"><span style=\"font-size: 18px;\">\n" +
            "          <span style=\"color: #f9a03e;\">Origen:</span> #ORIGEN </span> </p></div>\n" +
            "\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "\n" +
            "\n" +
            "      </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "          <!--[if mso | IE]>\n" +
            "            </td>\n" +
            "\n" +
            "            <td\n" +
            "               class=\"\" style=\"vertical-align:top;width:300px;\"\n" +
            "            >\n" +
            "          <![endif]-->\n" +
            "\n" +
            "      <div class=\"mj-column-per-50 outlook-group-fix\" style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:50%;\">\n" +
            "\n" +
            "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align:top;\" width=\"100%\">\n" +
            "\n" +
            "            <tr>\n" +
            "              <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "      <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
            "          <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: center;\">\n" +
            "              <span style=\"font-size: 18px;\">\n" +
            "                  <span style=\"color: #f9a03e;\">Destino:</span> #DESTINO</span> </p></div>\n" +
            "\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "\n" +
            "\n" +
            "      </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "          <!--[if mso | IE]>\n" +
            "            </td>\n" +
            "\n" +
            "        </tr>\n" +
            "\n" +
            "                  </table>\n" +
            "                <![endif]-->\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "          </tbody>\n" +
            "        </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "\n" +
            "      <!--[if mso | IE]>\n" +
            "          </td>\n" +
            "        </tr>\n" +
            "      </table>\n" +
            "\n" +
            "      <table\n" +
            "         align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n" +
            "      >\n" +
            "        <tr>\n" +
            "          <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
            "      <![endif]-->\n" +
            "\n" +
            "\n" +
            "      <div style=\"background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:80%;\">\n" +
            "\n" +
            "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background:#ffffff;background-color:#ffffff;width:100%;\">\n" +
            "          <tbody>\n" +
            "            <tr>\n" +
            "              <td style=\"direction:ltr;font-size:0px;padding:2px 0px 2px 0px;text-align:center;\">\n" +
            "                <!--[if mso | IE]>\n" +
            "                  <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "\n" +
            "        <tr>\n" +
            "\n" +
            "            <td\n" +
            "               class=\"\" style=\"vertical-align:top;width:600px;\"\n" +
            "            >\n" +
            "          <![endif]-->\n" +
            "\n" +
            "      <div class=\"mj-column-per-100 outlook-group-fix\" style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%;\">\n" +
            "\n" +
            "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align:top;\" width=\"100%\">\n" +
            "\n" +
            "            <tr>\n" +
            "              <td style=\"font-size:0px;padding:0px 5px;padding-top:5px;word-break:break-word;\">\n" +
            "\n" +
            "      <p style=\"font-family: Ubuntu, Helvetica, Arial; border-top: solid 2px #F9A03E; font-size: 1; margin: 0px auto; width: 100%;\">\n" +
            "      </p>\n" +
            "\n" +
            "      <!--[if mso | IE]>\n" +
            "        <table\n" +
            "           align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-top:solid 2px #F9A03E;font-size:1;margin:0px auto;width:580px;\" role=\"presentation\" width=\"580px\"\n" +
            "        >\n" +
            "          <tr>\n" +
            "            <td style=\"height:0;line-height:0;\">\n" +
            "              &nbsp;\n" +
            "            </td>\n" +
            "          </tr>\n" +
            "        </table>\n" +
            "      <![endif]-->\n" +
            "\n" +
            "\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "\n" +
            "      </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "          <!--[if mso | IE]>\n" +
            "            </td>\n" +
            "\n" +
            "        </tr>\n" +
            "\n" +
            "                  </table>\n" +
            "                <![endif]-->\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "          </tbody>\n" +
            "        </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "\n" +
            "      <!--[if mso | IE]>\n" +
            "          </td>\n" +
            "        </tr>\n" +
            "      </table>\n" +
            "\n" +
            "      <table\n" +
            "         align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n" +
            "      >\n" +
            "        <tr>\n" +
            "          <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
            "      <![endif]-->\n" +
            "\n" +
            "\n" +
            "      <div style=\"background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:80%;\">\n" +
            "\n" +
            "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background:#ffffff;background-color:#ffffff;width:100%;\">\n" +
            "          <tbody>\n" +
            "            <tr>\n" +
            "              <td style=\"direction:ltr;font-size:0px;padding:0px 0px 0px 0px;text-align:center;\">\n" +
            "                <!--[if mso | IE]>\n" +
            "                  <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "\n" +
            "        <tr>\n" +
            "\n" +
            "            <td\n" +
            "               class=\"\" style=\"vertical-align:top;width:600px;\"\n" +
            "            >\n" +
            "          <![endif]-->\n" +
            "\n" +
            "      <div class=\"mj-column-per-100 outlook-group-fix\" style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%;\">\n" +
            "\n" +
            "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align:top;\" width=\"100%\">\n" +
            "\n" +
            "            <tr>\n" +
            "              <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "      <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\"><p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px;\">\n" +
            "\n" +
            "\n" +
            "<!--          PAQUETES-->\n" +
            "                      <!-- inicio de paquetes-->\n" +
            "          <!-- inicio de paquetes-->\n" +
            "          #PAQUETES\n" +
            "          <!-- fin de paquetes -->\n" +
            "                      <!-- fin de paquetes -->\n" +
            "      </div>\n" +
            "\n" +
            "\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "\n" +
            "      </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "          <!--[if mso | IE]>\n" +
            "            </td>\n" +
            "\n" +
            "        </tr>\n" +
            "\n" +
            "                  </table>\n" +
            "                <![endif]-->\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "          </tbody>\n" +
            "        </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "\n" +
            "      <!--[if mso | IE]>\n" +
            "          </td>\n" +
            "        </tr>\n" +
            "      </table>\n" +
            "\n" +
            "\n" +
            "\n" +
            "      <table\n" +
            "         align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n" +
            "      >\n" +
            "        <tr>\n" +
            "          <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
            "      <![endif]-->\n" +
            "\n" +
            "\n" +
            "\n" +
            "      <!--[if mso | IE]>\n" +
            "          </td>\n" +
            "        </tr>\n" +
            "      </table>\n" +
            "\n" +
            "      <table\n" +
            "         align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n" +
            "      >\n" +
            "        <tr>\n" +
            "          <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
            "      <![endif]-->\n" +
            "\n" +
            "\n" +
            "\n" +
            "      <!--[if mso | IE]>\n" +
            "          </td>\n" +
            "        </tr>\n" +
            "      </table>\n" +
            "\n" +
            "      <table\n" +
            "         align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n" +
            "      >\n" +
            "        <tr>\n" +
            "          <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
            "      <![endif]-->\n" +
            "\n" +
            "\n" +
            "      <div style=\"background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:80%;\">\n" +
            "\n" +
            "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background:#ffffff;background-color:#ffffff;width:100%;\">\n" +
            "          <tbody>\n" +
            "            <tr>\n" +
            "              <td style=\"direction:ltr;font-size:0px;padding:0px 0px 0px 0px;text-align:center;\">\n" +
            "                <!--[if mso | IE]>\n" +
            "                  <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "\n" +
            "        <tr>\n" +
            "\n" +
            "            <td\n" +
            "               class=\"\" style=\"vertical-align:top;width:600px;\"\n" +
            "            >\n" +
            "          <![endif]-->\n" +
            "\n" +
            "      <div class=\"mj-column-per-100 outlook-group-fix\" style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%;\">\n" +
            "\n" +
            "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" width=\"100%\">\n" +
            "        <tbody>\n" +
            "          <tr>\n" +
            "            <td style=\"vertical-align:top;padding:0px 0px 10px 0px;\">\n" +
            "\n" +
            "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style width=\"100%\">\n" +
            "\n" +
            "            <tr>\n" +
            "              <td style=\"font-size:0px;word-break:break-word;\">\n" +
            "\n" +
            "\n" +
            "    <!--[if mso | IE]>\n" +
            "\n" +
            "        <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td height=\"20\" style=\"vertical-align:top;height:20px;\">\n" +
            "\n" +
            "    <![endif]-->\n" +
            "\n" +
            "      <div style=\"height:20px;\">\n" +
            "        &nbsp;\n" +
            "      </div>\n" +
            "\n" +
            "    <!--[if mso | IE]>\n" +
            "\n" +
            "        </td></tr></table>\n" +
            "\n" +
            "    <![endif]-->\n" +
            "\n" +
            "\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "\n" +
            "      </table>\n" +
            "\n" +
            "            </td>\n" +
            "          </tr>\n" +
            "        </tbody>\n" +
            "      </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "          <!--[if mso | IE]>\n" +
            "            </td>\n" +
            "\n" +
            "        </tr>\n" +
            "\n" +
            "                  </table>\n" +
            "                <![endif]-->\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "          </tbody>\n" +
            "        </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "\n" +
            "      <!--[if mso | IE]>\n" +
            "          </td>\n" +
            "        </tr>\n" +
            "      </table>\n" +
            "      <![endif]-->\n" +
            "\n" +
            "\n" +
            "      <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"background:#f0f0f0;background-color:#f0f0f0;width:100%;\">\n" +
            "        <tbody>\n" +
            "          <tr>\n" +
            "            <td>\n" +
            "\n" +
            "\n" +
            "      <!--[if mso | IE]>\n" +
            "      <table\n" +
            "         align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n" +
            "      >\n" +
            "        <tr>\n" +
            "          <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
            "      <![endif]-->\n" +
            "\n" +
            "\n" +
            "      <div style=\"margin:0px auto;max-width:80%;\">\n" +
            "\n" +
            "        <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"width:100%;\">\n" +
            "          <tbody>\n" +
            "            <tr>\n" +
            "              <td style=\"direction:ltr;font-size:0px;padding:5px 0px 5px 0px;text-align:center;\">\n" +
            "                <!--[if mso | IE]>\n" +
            "                  <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "\n" +
            "        <tr>\n" +
            "\n" +
            "            <td\n" +
            "               class=\"\" style=\"vertical-align:middle;width:399.99999999999994px;\"\n" +
            "            >\n" +
            "          <![endif]-->\n" +
            "\n" +
            "      <div class=\"mj-column-per-66 outlook-group-fix\" style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:middle;width:66.66666666666666%;\">\n" +
            "\n" +
            "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align:middle;\" width=\"100%\">\n" +
            "\n" +
            "            <tr>\n" +
            "              <td style=\"font-size:0px;word-break:break-word;\">\n" +
            "\n" +
            "\n" +
            "    <!--[if mso | IE]>\n" +
            "\n" +
            "        <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td height=\"10\" style=\"vertical-align:top;height:10px;\">\n" +
            "\n" +
            "    <![endif]-->\n" +
            "\n" +
            "      <div style=\"height:10px;\">\n" +
            "        &nbsp;\n" +
            "      </div>\n" +
            "\n" +
            "    <!--[if mso | IE]>\n" +
            "\n" +
            "        </td></tr></table>\n" +
            "\n" +
            "    <![endif]-->\n" +
            "\n" +
            "\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "\n" +
            "      </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "          <!--[if mso | IE]>\n" +
            "            </td>\n" +
            "\n" +
            "            <td\n" +
            "               class=\"\" style=\"vertical-align:middle;width:199.99999999999997px;\"\n" +
            "            >\n" +
            "          <![endif]-->\n" +
            "\n" +
            "      <div class=\"mj-column-per-33 outlook-group-fix\" style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:middle;width:33.33333333333333%;\">\n" +
            "\n" +
            "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"vertical-align:middle;\" width=\"100%\">\n" +
            "\n" +
            "            <tr>\n" +
            "              <td style=\"font-size:0px;word-break:break-word;\">\n" +
            "\n" +
            "\n" +
            "    <!--[if mso | IE]>\n" +
            "\n" +
            "        <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"><tr><td height=\"10\" style=\"vertical-align:top;height:10px;\">\n" +
            "\n" +
            "    <![endif]-->\n" +
            "\n" +
            "      <div style=\"height:10px;\">\n" +
            "        &nbsp;\n" +
            "      </div>\n" +
            "\n" +
            "    <!--[if mso | IE]>\n" +
            "\n" +
            "        </td></tr></table>\n" +
            "\n" +
            "    <![endif]-->\n" +
            "\n" +
            "\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "\n" +
            "      </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "          <!--[if mso | IE]>\n" +
            "            </td>\n" +
            "\n" +
            "        </tr>\n" +
            "\n" +
            "                  </table>\n" +
            "                <![endif]-->\n" +
            "              </td>\n" +
            "            </tr>\n" +
            "          </tbody>\n" +
            "        </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "\n" +
            "      <!--[if mso | IE]>\n" +
            "          </td>\n" +
            "        </tr>\n" +
            "      </table>\n" +
            "      <![endif]-->\n" +
            "\n" +
            "\n" +
            "            </td>\n" +
            "          </tr>\n" +
            "        </tbody>\n" +
            "      </table>\n" +
            "\n" +
            "      </div>\n" +
            "\n" +
            "      </body>\n" +
            "    </html>" +
            "";


    public static String htmlPaquetes = "<div>\n" +
            "\n" +
            "              <div style=\"background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:600px;\">\n" +
            "\n" +
            "                  <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
            "                         style=\"background:#ffffff;background-color:#ffffff;width:100%;\">\n" +
            "                      <tbody>\n" +
            "                      <tr>\n" +
            "                          <td style=\"direction:ltr;font-size:0px;padding:1px 0px 1px 0px;text-align:center;\">\n" +
            "                              <!--[if mso | IE]>\n" +
            "                              <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "\n" +
            "                                  <tr>\n" +
            "\n" +
            "                                      <td\n" +
            "                                              class=\"\" style=\"vertical-align:top;width:600px;\"\n" +
            "                                      >\n" +
            "                              <![endif]-->\n" +
            "\n" +
            "                              <div class=\"mj-column-per-100 outlook-group-fix\"\n" +
            "                                   style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%;\">\n" +
            "\n" +
            "                                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
            "                                         style=\"vertical-align:top;\" width=\"100%\">\n" +
            "\n" +
            "                                      <tr>\n" +
            "                                          <td align=\"left\"\n" +
            "                                              style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "                                              <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
            "                                                  <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px;\"><strong><span\n" +
            "                                                          style=\"font-size: 16px;\">Paquete #NUM_PAQUETE</span></strong></p></div>\n" +
            "\n" +
            "                                          </td>\n" +
            "                                      </tr>\n" +
            "\n" +
            "                                  </table>\n" +
            "\n" +
            "                              </div>\n" +
            "\n" +
            "                              <!--[if mso | IE]>\n" +
            "                              </td>\n" +
            "\n" +
            "                              </tr>\n" +
            "\n" +
            "                              </table>\n" +
            "                              <![endif]-->\n" +
            "                          </td>\n" +
            "                      </tr>\n" +
            "                      </tbody>\n" +
            "                  </table>\n" +
            "\n" +
            "              </div>\n" +
            "\n" +
            "\n" +
            "              <!--[if mso | IE]>\n" +
            "              </td>\n" +
            "              </tr>\n" +
            "              </table>\n" +
            "\n" +
            "              <table\n" +
            "                      align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n" +
            "              >\n" +
            "                  <tr>\n" +
            "                      <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
            "              <![endif]-->\n" +
            "\n" +
            "\n" +
            "              <div style=\"background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:600px;\">\n" +
            "\n" +
            "                  <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
            "                         style=\"background:#ffffff;background-color:#ffffff;width:100%;\">\n" +
            "                      <tbody>\n" +
            "                      <tr>\n" +
            "                          <td style=\"direction:ltr;font-size:0px;padding:1px 0px 1px 0px;text-align:center;\">\n" +
            "                              <!--[if mso | IE]>\n" +
            "                              <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "\n" +
            "                                  <tr>\n" +
            "\n" +
            "                                      <td\n" +
            "                                              class=\"\" style=\"vertical-align:top;width:150px;\"\n" +
            "                                      >\n" +
            "                              <![endif]-->\n" +
            "\n" +
            "                              <div class=\"mj-column-per-25 outlook-group-fix\"\n" +
            "                                   style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:25%;\">\n" +
            "\n" +
            "                                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
            "                                         style=\"vertical-align:top;\" width=\"100%\">\n" +
            "\n" +
            "                                      <tr>\n" +
            "                                          <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "                                              <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
            "                                                  <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
            "                                                      <span style=\"font-size: 14px;\"><b>Peso</b></span></p></div>\n" +
            "\n" +
            "                                          </td>\n" +
            "                                      </tr>\n" +
            "\n" +
            "                                      <tr>\n" +
            "                                          <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "                                              <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
            "                                                  <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
            "                                                      <span style=\"font-size: 14px;\">#PESO kg</span></p></div>\n" +
            "\n" +
            "                                          </td>\n" +
            "                                      </tr>\n" +
            "\n" +
            "                                  </table>\n" +
            "\n" +
            "                              </div>\n" +
            "\n" +
            "                              <!--[if mso | IE]>\n" +
            "                              </td>\n" +
            "\n" +
            "                              <td\n" +
            "                                      class=\"\" style=\"vertical-align:top;width:150px;\"\n" +
            "                              >\n" +
            "                              <![endif]-->\n" +
            "\n" +
            "                              <div class=\"mj-column-per-25 outlook-group-fix\"\n" +
            "                                   style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:25%;\">\n" +
            "\n" +
            "                                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
            "                                         style=\"vertical-align:top;\" width=\"100%\">\n" +
            "\n" +
            "                                      <tr>\n" +
            "                                          <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "                                              <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
            "                                                  <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
            "                                                      <span style=\"font-size: 14px;\"><b>Largo</b></span></p></div>\n" +
            "\n" +
            "                                          </td>\n" +
            "                                      </tr>\n" +
            "\n" +
            "                                      <tr>\n" +
            "                                          <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "                                              <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
            "                                                  <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
            "                                                      <span style=\"font-size: 14px;\">#LARGO cm</span></p></div>\n" +
            "\n" +
            "                                          </td>\n" +
            "                                      </tr>\n" +
            "\n" +
            "                                  </table>\n" +
            "\n" +
            "                              </div>\n" +
            "\n" +
            "                              <!--[if mso | IE]>\n" +
            "                              </td>\n" +
            "\n" +
            "                              <td\n" +
            "                                      class=\"\" style=\"vertical-align:top;width:150px;\"\n" +
            "                              >\n" +
            "                              <![endif]-->\n" +
            "\n" +
            "                              <div class=\"mj-column-per-25 outlook-group-fix\"\n" +
            "                                   style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:25%;\">\n" +
            "\n" +
            "                                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
            "                                         style=\"vertical-align:top;\" width=\"100%\">\n" +
            "\n" +
            "                                      <tr>\n" +
            "                                          <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "                                              <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
            "                                                  <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
            "                                                      <span style=\"font-size: 14px;\"><b>Ancho</b></span></p></div>\n" +
            "\n" +
            "                                          </td>\n" +
            "                                      </tr>\n" +
            "\n" +
            "                                      <tr>\n" +
            "                                          <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "                                              <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
            "                                                  <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
            "                                                      <span style=\"font-size: 14px;\">#ANCHO cm</span></p></div>\n" +
            "\n" +
            "                                          </td>\n" +
            "                                      </tr>\n" +
            "\n" +
            "                                  </table>\n" +
            "\n" +
            "                              </div>\n" +
            "\n" +
            "                              <!--[if mso | IE]>\n" +
            "                              </td>\n" +
            "\n" +
            "                              <td\n" +
            "                                      class=\"\" style=\"vertical-align:top;width:150px;\"\n" +
            "                              >\n" +
            "                              <![endif]-->\n" +
            "\n" +
            "                              <div class=\"mj-column-per-25 outlook-group-fix\"\n" +
            "                                   style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:25%;\">\n" +
            "\n" +
            "                                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
            "                                         style=\"vertical-align:top;\" width=\"100%\">\n" +
            "\n" +
            "                                      <tr>\n" +
            "                                          <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "                                              <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
            "                                                  <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
            "                                                      <span style=\"font-size: 14px;\"><b>Alto</b></span></p></div>\n" +
            "\n" +
            "                                          </td>\n" +
            "                                      </tr>\n" +
            "\n" +
            "                                      <tr>\n" +
            "                                          <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "                                              <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
            "                                                  <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
            "                                                      <span style=\"font-size: 14px;\">#ALTO cm</span></p></div>\n" +
            "\n" +
            "                                          </td>\n" +
            "                                      </tr>\n" +
            "\n" +
            "                                  </table>\n" +
            "\n" +
            "                              </div>\n" +
            "\n" +
            "                              <!--[if mso | IE]>\n" +
            "                              </td>\n" +
            "\n" +
            "                              </tr>\n" +
            "\n" +
            "                              </table>\n" +
            "                              <![endif]-->\n" +
            "                          </td>\n" +
            "                      </tr>\n" +
            "                      </tbody>\n" +
            "                  </table>\n" +
            "\n" +
            "              </div>\n" +
            "\n" +
            "\n" +
            "              <!--[if mso | IE]>\n" +
            "              </td>\n" +
            "              </tr>\n" +
            "              </table>\n" +
            "\n" +
            "              <table\n" +
            "                      align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n" +
            "              >\n" +
            "                  <tr>\n" +
            "                      <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
            "              <![endif]-->\n" +
            "\n" +
            "\n" +
            "              <div style=\"background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:600px;\">\n" +
            "\n" +
            "                  <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
            "                         style=\"background:#ffffff;background-color:#ffffff;width:100%;\">\n" +
            "                      <tbody>\n" +
            "                      <tr>\n" +
            "                          <td style=\"direction:ltr;font-size:0px;padding:1px 0px 1px 0px;text-align:center;\">\n" +
            "                              <!--[if mso | IE]>\n" +
            "                              <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "\n" +
            "                                  <tr>\n" +
            "\n" +
            "                                      <td\n" +
            "                                              class=\"\" style=\"vertical-align:top;width:300px;\"\n" +
            "                                      >\n" +
            "                              <![endif]-->\n" +
            "\n" +
            "                              <div class=\"mj-column-per-50 outlook-group-fix\"\n" +
            "                                   style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:50%;\">\n" +
            "\n" +
            "                                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
            "                                         style=\"vertical-align:top;\" width=\"100%\">\n" +
            "\n" +
            "                                      <tr>\n" +
            "                                          <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "                                              <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
            "                                                  <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
            "                                                      <span style=\"font-size: 14px;\"><b>Descripci&oacute;n</b></span></p></div>\n" +
            "\n" +
            "                                          </td>\n" +
            "                                      </tr>\n" +
            "\n" +
            "                                      <tr>\n" +
            "                                          <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "                                              <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
            "                                                  <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
            "                                                      <span style=\"font-size: 14px;\">#DESCRIPCION</span></p></div>\n" +
            "\n" +
            "                                          </td>\n" +
            "                                      </tr>\n" +
            "\n" +
            "                                  </table>\n" +
            "\n" +
            "                              </div>\n" +
            "\n" +
            "                              <!--[if mso | IE]>\n" +
            "                              </td>\n" +
            "\n" +
            "                              <td\n" +
            "                                      class=\"\" style=\"vertical-align:top;width:300px;\"\n" +
            "                              >\n" +
            "                              <![endif]-->\n" +
            "\n" +
            "                              <div class=\"mj-column-per-50 outlook-group-fix\"\n" +
            "                                   style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:50%;\">\n" +
            "\n" +
            "                                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
            "                                         style=\"vertical-align:top;\" width=\"100%\">\n" +
            "\n" +
            "                                      <tr>\n" +
            "                                          <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "                                              <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
            "                                                  <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: right;\">\n" +
            "                                                      <span style=\"font-size: 14px;\"><b>Ctd</b></span></p></div>\n" +
            "\n" +
            "                                          </td>\n" +
            "                                      </tr>\n" +
            "\n" +
            "                                      <tr>\n" +
            "                                          <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "                                              <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
            "                                                  <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: right;\">\n" +
            "                                                      <span style=\"font-size: 14px;\">#CTD</span></p></div>\n" +
            "\n" +
            "                                          </td>\n" +
            "                                      </tr>\n" +
            "\n" +
            "                                  </table>\n" +
            "\n" +
            "                              </div>\n" +
            "\n" +
            "                              <!--[if mso | IE]>\n" +
            "                              </td>\n" +
            "\n" +
            "                              </tr>\n" +
            "\n" +
            "                              </table>\n" +
            "                              <![endif]-->\n" +
            "                          </td>\n" +
            "                      </tr>\n" +
            "                      </tbody>\n" +
            "                  </table>\n" +
            "\n" +
            "              </div>\n" +
            "\n" +
            "\n" +
            "              <!--[if mso | IE]>\n" +
            "              </td>\n" +
            "              </tr>\n" +
            "              </table>\n" +
            "\n" +
            "              <table\n" +
            "                      align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n" +
            "              >\n" +
            "                  <tr>\n" +
            "                      <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
            "              <![endif]-->\n" +
            "\n" +
            "\n" +
            "              <div style=\"background:#ffffff;background-color:#ffffff;margin:0px auto;max-width:600px;\">\n" +
            "\n" +
            "                  <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
            "                         style=\"background:#ffffff;background-color:#ffffff;width:100%;\">\n" +
            "                      <tbody>\n" +
            "                      <tr>\n" +
            "                          <td style=\"direction:ltr;font-size:0px;padding:1px 0px 1px 0px;text-align:center;\">\n" +
            "                              <!--[if mso | IE]>\n" +
            "                              <table role=\"presentation\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "\n" +
            "                                  <tr>\n" +
            "\n" +
            "                                      <td\n" +
            "                                              class=\"\" style=\"vertical-align:top;width:600px;\"\n" +
            "                                      >\n" +
            "                              <![endif]-->\n" +
            "\n" +
            "                              <div class=\"mj-column-per-100 outlook-group-fix\"\n" +
            "                                   style=\"font-size:0px;text-align:left;direction:ltr;display:inline-block;vertical-align:top;width:100%;\">\n" +
            "\n" +
            "                                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\"\n" +
            "                                         style=\"vertical-align:top;\" width=\"100%\">\n" +
            "\n" +
            "                                      <tr>\n" +
            "                                          <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "                                              <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
            "                                                  <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
            "                                                      <span style=\"font-size: 14px;\"><b>Observaciones</b></span></p></div>\n" +
            "\n" +
            "                                          </td>\n" +
            "                                      </tr>\n" +
            "\n" +
            "                                      <tr>\n" +
            "                                          <td align=\"left\" style=\"font-size:0px;padding:0px 15px 0px 15px;word-break:break-word;\">\n" +
            "\n" +
            "                                              <div style=\"font-family:Ubuntu, Helvetica, Arial, sans-serif;font-size:13px;line-height:1.5;text-align:left;color:#000000;\">\n" +
            "                                                  <p style=\"font-family: Ubuntu, Helvetica, Arial; font-size: 11px; text-align: left;\">\n" +
            "                                                      <span style=\"font-size: 14px;\">#OBSERVACIONES</span></p></div>\n" +
            "\n" +
            "                                          </td>\n" +
            "                                      </tr>\n" +
            "\n" +
            "                                  </table>\n" +
            "\n" +
            "                              </div>\n" +
            "\n" +
            "                              <!--[if mso | IE]>\n" +
            "                              </td>\n" +
            "\n" +
            "                              </tr>\n" +
            "\n" +
            "                              </table>\n" +
            "                              <![endif]-->\n" +
            "                          </td>\n" +
            "                      </tr>\n" +
            "                      </tbody>\n" +
            "                  </table>\n" +
            "\n" +
            "              </div>\n" +
            "\n" +
            "\n" +
            "              <!--[if mso | IE]>\n" +
            "              </td>\n" +
            "              </tr>\n" +
            "              </table>\n" +
            "\n" +
            "              <table\n" +
            "                      align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"\" style=\"width:600px;\" width=\"600\"\n" +
            "              >\n" +
            "                  <tr>\n" +
            "                      <td style=\"line-height:0px;font-size:0px;mso-line-height-rule:exactly;\">\n" +
            "              <![endif]-->\n" +
            "          </div>";

    public static String htmlBitacora = "  <li class=\"tl-item\" ng-repeat=\"item in retailer_history\">\n" +
            "                                  <div class=\"timestamp\">\n" +
            "                                      #Fecha<br> #Hora\n" +
            "                                  </div>\n" +
            "                                  <div class=\"item-title\">#DESCRIPCION</div>\n" +
            "                                  <div class=\"item-detail\">#DETALLE</div>\n" +
            "                              </li>";

    public static String htmlEstatus = "<li class=\"vcv-timeline-item #COMPLETADO\" data-step=\"#INDEX\"><span>#ESTATUS</span></li>";
}
