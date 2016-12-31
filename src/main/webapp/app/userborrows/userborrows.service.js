(function() {
    'use strict';
    angular
        .module('aiProjektApp')
        .factory('UserBorrows', UserBorrows);

    UserBorrows.$inject = ['$resource'];

    function UserBorrows ($resource) {
        var resourceUrl =  'api/borrows/user/user-2/';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
