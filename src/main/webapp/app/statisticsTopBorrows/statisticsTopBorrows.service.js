(function() {
    'use strict';
    angular
        .module('aiProjektApp')
        .factory('StatisticsTopBorrows', StatisticsTopBorrows);

    StatisticsTopBorrows.$inject = ['$resource'];

    function StatisticsTopBorrows ($resource) {
        var resourceUrl =  'api/statistics/borrows';

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
