(function() {
    'use strict';
    angular
        .module('aiProjektApp')
        .factory('BookIsbn', BookIsbn);

    BookIsbn.$inject = ['$resource'];

    function BookIsbn ($resource) {
        var resourceUrl =  'api/booksIsbn/:id';

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
