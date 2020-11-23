'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol ? "symbol" : typeof obj; };

exports.default = function (defaultLibraryName) {
  return function (_ref) {
    var types = _ref.types;

    var specified = void 0;
    var libraryObjs = void 0;
    var selectedMethods = void 0;
    var moduleArr = void 0;

    function parseName(_str, camel2Dash) {
      if (!camel2Dash) {
        return _str;
      }
      var str = _str[0].toLowerCase() + _str.substr(1);
      return str.replace(/([A-Z])/g, function ($1) {
        return '-' + $1.toLowerCase();
      });
    }

    function importMethod(methodName, file, opts) {
      if (!selectedMethods[methodName]) {
        var options = void 0;
        var path = void 0;

        if (Array.isArray(opts)) {
          options = opts.find(function (option) {
            return moduleArr[methodName] === option.libraryName || libraryObjs[methodName] === option.libraryName;
          }); // eslint-disable-line
        }
        options = options || opts;

        var _options = options;
        var _options$libDir = _options.libDir;
        var libDir = _options$libDir === undefined ? 'lib' : _options$libDir;
        var _options$libraryName = _options.libraryName;
        var libraryName = _options$libraryName === undefined ? defaultLibraryName : _options$libraryName;
        var style = _options.style;
        var styleLibrary = _options.styleLibrary;
        var _options$root = _options.root;
        var root = _options$root === undefined ? '' : _options$root;
        var _options$camel2Dash = _options.camel2Dash;
        var camel2Dash = _options$camel2Dash === undefined ? true : _options$camel2Dash;

        var styleLibraryName = options.styleLibraryName;
        var _root = root;
        var isBaseStyle = true;
        var modulePathTpl = void 0;
        var mixin = false;

        if (root) {
          _root = '/' + root;
        }

        if (libraryObjs[methodName]) {
          path = libraryName + '/' + libDir + _root;
          if (!_root) {
            importAll[path] = true;
          }
        } else {
          path = libraryName + '/' + libDir + '/' + parseName(methodName, camel2Dash);
        }
        var _path = path;

        selectedMethods[methodName] = file.addImport(path, 'default');
        if (styleLibrary && (typeof styleLibrary === 'undefined' ? 'undefined' : _typeof(styleLibrary)) === 'object') {
          styleLibraryName = styleLibrary.name;
          isBaseStyle = styleLibrary.base;
          modulePathTpl = styleLibrary.path;
          mixin = styleLibrary.mixin;
        }
        if (styleLibraryName) {
          if (!cachePath[libraryName]) {
            var themeName = styleLibraryName.replace(/^~/, '');
            cachePath[libraryName] = styleLibraryName.indexOf('~') === 0 ? resolve(process.cwd(), themeName) : libraryName + '/' + libDir + '/' + themeName;
          }

          if (libraryObjs[methodName]) {
            /* istanbul ingore next */
            if (cache[libraryName] === 2) {
              throw Error('[babel-plugin-component] If you are using both' + 'on-demand and importing all, make sure to invoke the' + ' importing all first.');
            }
            path = '' + cachePath[libraryName] + (_root || '/index') + '.css';
            cache[libraryName] = 1;
          } else {
            if (cache[libraryName] !== 1) {
              /* if set styleLibrary.path(format: [module]/module.css) */
              var parsedMethodName = parseName(methodName, camel2Dash);
              if (modulePathTpl) {
                var modulePath = modulePathTpl.replace(/\[module]/ig, parsedMethodName);
                path = cachePath[libraryName] + '/' + modulePath;
              } else {
                path = cachePath[libraryName] + '/' + parsedMethodName + '.css';
              }
              if (mixin && !isExist(path)) {
                path = style === true ? _path + '/style.css' : _path + '/' + style;
              }
              if (isBaseStyle) {
                file.addImport(cachePath[libraryName] + '/base.css', 'default');
              }
              cache[libraryName] = 2;
            }
          }

          file.addImport(path, 'default');
        } else {
          if (style === true) {
            file.addImport(path + '/style.css', 'default');
          } else if (style) {
            file.addImport(path + '/' + style, 'default');
          }
        }
      }
      return selectedMethods[methodName];
    }

    function buildExpressionHandler(node, props, path, opts) {
      var file = path.hub.file;

      props.forEach(function (prop) {
        if (!types.isIdentifier(node[prop])) return;
        if (specified[node[prop].name]) {
          node[prop] = importMethod(node[prop].name, file, opts); // eslint-disable-line
        }
      });
    }

    function buildDeclaratorHandler(node, prop, path, opts) {
      var file = path.hub.file;

      if (!types.isIdentifier(node[prop])) return;
      if (specified[node[prop].name]) {
        node[prop] = importMethod(node[prop].name, file, opts); // eslint-disable-line
      }
    }

    return {
      visitor: {
        Program: function Program() {
          specified = Object.create(null);
          libraryObjs = Object.create(null);
          selectedMethods = Object.create(null);
          moduleArr = Object.create(null);
        },
        ImportDeclaration: function ImportDeclaration(path, _ref2) {
          var opts = _ref2.opts;
          var node = path.node;
          var value = node.source.value;

          var result = {};

          if (Array.isArray(opts)) {
            result = opts.find(function (option) {
              return option.libraryName === value;
            }) || {};
          }
          var libraryName = result.libraryName || opts.libraryName || defaultLibraryName;

          if (value === libraryName) {
            node.specifiers.forEach(function (spec) {
              if (types.isImportSpecifier(spec)) {
                specified[spec.local.name] = spec.imported.name;
                moduleArr[spec.imported.name] = value;
              } else {
                libraryObjs[spec.local.name] = value;
              }
            });

            if (!importAll[value]) {
              path.remove();
            }
          }
        },
        CallExpression: function CallExpression(path, _ref3) {
          var opts = _ref3.opts;
          var node = path.node;
          var file = path.hub.file;
          var name = node.callee.name;


          if (types.isIdentifier(node.callee)) {
            if (specified[name]) {
              node.callee = importMethod(specified[name], file, opts);
            }
          } else {
            node.arguments = node.arguments.map(function (arg) {
              var argName = arg.name;

              if (specified[argName]) {
                return importMethod(specified[argName], file, opts);
              } else if (libraryObjs[argName]) {
                return importMethod(argName, file, opts);
              }
              return arg;
            });
          }
        },
        MemberExpression: function MemberExpression(path, _ref4) {
          var opts = _ref4.opts;
          var node = path.node;
          var file = path.hub.file;


          if (libraryObjs[node.object.name] || specified[node.object.name]) {
            node.object = importMethod(node.object.name, file, opts);
          }
        },
        AssignmentExpression: function AssignmentExpression(path, _ref5) {
          var opts = _ref5.opts;
          var node = path.node;
          var file = path.hub.file;


          if (node.operator !== '=') return;
          if (libraryObjs[node.right.name] || specified[node.right.name]) {
            node.right = importMethod(node.right.name, file, opts);
          }
        },
        ArrayExpression: function ArrayExpression(path, _ref6) {
          var opts = _ref6.opts;
          var elements = path.node.elements;
          var file = path.hub.file;


          elements.forEach(function (item, key) {
            if (item && (libraryObjs[item.name] || specified[item.name])) {
              elements[key] = importMethod(item.name, file, opts);
            }
          });
        },
        Property: function Property(path, _ref7) {
          var opts = _ref7.opts;
          var node = path.node;

          buildDeclaratorHandler(node, 'value', path, opts);
        },
        VariableDeclarator: function VariableDeclarator(path, _ref8) {
          var opts = _ref8.opts;
          var node = path.node;

          buildDeclaratorHandler(node, 'init', path, opts);
        },
        LogicalExpression: function LogicalExpression(path, _ref9) {
          var opts = _ref9.opts;
          var node = path.node;

          buildExpressionHandler(node, ['left', 'right'], path, opts);
        },
        ConditionalExpression: function ConditionalExpression(path, _ref10) {
          var opts = _ref10.opts;
          var node = path.node;

          buildExpressionHandler(node, ['test', 'consequent', 'alternate'], path, opts);
        },
        IfStatement: function IfStatement(path, _ref11) {
          var opts = _ref11.opts;
          var node = path.node;

          buildExpressionHandler(node, ['test'], path, opts);
          buildExpressionHandler(node.test, ['left', 'right'], path, opts);
        }
      }
    };
  };
};

var resolve = require('path').resolve;
var isExist = require('fs').existsSync;
var cache = {};
var cachePath = {};
var importAll = {};

module.exports = exports['default'];